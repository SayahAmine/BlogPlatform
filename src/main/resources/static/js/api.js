// ---- Auth helpers ----
const Auth = {
    saveToken(token, user) {
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));
    },
    getToken() { return localStorage.getItem('token'); },
    getUser() {
        const u = localStorage.getItem('user');
        return u ? JSON.parse(u) : null;
    },
    isLoggedIn() { return !!this.getToken(); },
    clear() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    },
    logout() {
        this.clear();
        window.location.href = '/login';
    }
};

// ---- Session validation ----
// Call this on every page load to verify the token is still valid
// against the backend. If the token is expired, tampered with, or
// signed with a stale key (e.g. after a server restart with an
// ephemeral signing key), the backend returns 401 and we clear it.
async function validateSession() {
    if (!Auth.isLoggedIn()) return false;
    try {
        const user = await apiFetch('/api/v1/auth/me');
        // Refresh cached user data (name may have changed, etc.)
        Auth.saveToken(Auth.getToken(), user);
        return true;
    } catch {
        Auth.clear();
        return false;
    }
}

// ---- Fetch wrapper ----
async function apiFetch(path, options = {}) {
    const headers = {
        'Content-Type': 'application/json',
        ...(options.headers || {})
    };
    const token = Auth.getToken();
    if (token) headers['Authorization'] = `Bearer ${token}`;

    const res = await fetch(path, { ...options, headers });

    // 401 = token missing/invalid/expired → session is dead, log out.
    // 403 = authenticated but not authorized → do NOT log out; let it
    //       bubble up so the caller can show "you can't do that".
    if (res.status === 401 && !path.includes('/auth/')) {
        Auth.logout();
        throw new Error('Session expired. Please log in again.');
    }

    if (res.status === 204) return null;
    const text = await res.text();
    const data = text ? JSON.parse(text) : null;

    if (!res.ok) {
        const msg = data?.message || data?.error || `Request failed (${res.status})`;
        throw new Error(msg);
    }
    return data;
}

// ---- Toast ----
function showToast(message, type = 'info') {
    let toast = document.getElementById('toast');
    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'toast';
        toast.className = 'toast';
        document.body.appendChild(toast);
    }
    toast.className = `toast ${type}`;
    toast.textContent = message;
    toast.classList.add('show');
    clearTimeout(toast._timer);
    toast._timer = setTimeout(() => toast.classList.remove('show'), 3500);
}

// ---- Navbar renderer ----
function renderNavbar() {
    const user = Auth.getUser();
    const isLogged = Auth.isLoggedIn();
    const path = window.location.pathname;

    const link = (href, label) =>
        `<a href="${href}" class="${path === href ? 'active' : ''}">${label}</a>`;

    const html = `
        <nav class="navbar">
            <a href="/" class="brand">📝 BlogPlatform</a>
            <div class="nav-links">
                ${link('/', 'Home')}
                ${link('/categories', 'Categories')}
                ${link('/tags', 'Tags')}
                ${isLogged ? `
                    ${link('/posts/new', 'New Post')}
                    ${link('/drafts', 'My Drafts')}
                    <span class="nav-user">Hi, ${user?.name || 'User'}</span>
                    <button class="btn btn-sm btn-secondary" onclick="Auth.logout()">Logout</button>
                ` : `
                    <a href="/login" class="btn btn-sm">Login</a>
                `}
            </div>
        </nav>
    `;
    document.body.insertAdjacentHTML('afterbegin', html);
}