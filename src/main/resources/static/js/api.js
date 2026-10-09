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
    logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/login';
    }
};

// ---- Fetch wrapper ----
async function apiFetch(path, options = {}) {
    const headers = {
        'Content-Type': 'application/json',
        ...(options.headers || {})
    };
    const token = Auth.getToken();
    if (token) headers['Authorization'] = `Bearer ${token}`;

    const res = await fetch(path, { ...options, headers });

    if (res.status === 401 || res.status === 403) {
        // Token expired/invalid
        if (!path.includes('/auth/')) {
            Auth.logout();
            throw new Error('Session expired. Please log in again.');
        }
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

    const html = `
        <nav class="navbar">
            <a href="/" class="brand">📝 BlogPlatform</a>
            <div class="nav-links">
                <a href="/" class="${path === '/' ? 'active' : ''}">Home</a>
                ${isLogged ? `
                    <a href="/posts/new" class="${path === '/posts/new' ? 'active' : ''}">New Post</a>
                    <a href="/drafts" class="${path === '/drafts' ? 'active' : ''}">My Drafts</a>
                    <span style="color: var(--text-muted)">Hi, ${user?.name || 'User'}</span>
                    <button class="btn btn-sm btn-secondary" onclick="Auth.logout()">Logout</button>
                ` : `
                    <a href="/login" class="btn btn-sm">Login</a>
                `}
            </div>
        </nav>
    `;
    document.body.insertAdjacentHTML('afterbegin', html);
}