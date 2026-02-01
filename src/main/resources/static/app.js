// SA Demo UI - Enhanced JavaScript
// ========================================

const logEl = document.getElementById("log-output");

// Toast notification system
function showToast(message, type = "success") {
  const existing = document.querySelector(".toast");
  if (existing) existing.remove();

  const toast = document.createElement("div");
  toast.className = `toast ${type}`;
  toast.textContent = message;
  document.body.appendChild(toast);

  setTimeout(() => toast.classList.add("show"), 10);
  setTimeout(() => {
    toast.classList.remove("show");
    setTimeout(() => toast.remove(), 300);
  }, 3000);
}

// Enhanced logging
function log(message, type = "info") {
  const line = document.createElement("div");
  line.className = `log-line${type === "error" ? " log-line--error" : ""}`;
  const time = new Date().toLocaleTimeString();
  const icon = type === "error" ? "❌" : "📡";
  line.innerHTML = `<time>[${time}]</time> ${icon} <code>${escapeHtml(message)}</code>`;
  logEl.prepend(line);
}

function escapeHtml(text) {
  const div = document.createElement("div");
  div.textContent = text;
  return div.innerHTML;
}

// API Request wrapper with enhanced UX
async function apiRequest(path, options = {}) {
  const url = path.startsWith("http") ? path : path.replace(/^\/?/, "/");
  const method = options.method || "GET";
  log(`${method} ${url}`);

  try {
    const res = await fetch(url, {
      headers: {
        "Content-Type": "application/json",
        ...(options.headers || {}),
      },
      ...options,
    });

    const text = await res.text();
    let json;
    try {
      json = text ? JSON.parse(text) : null;
    } catch {
      json = text;
    }

    const statusIcon = res.ok ? "✅" : "⚠️";
    log(`${statusIcon} ${res.status} ${res.statusText} ← ${url}`);

    if (!res.ok) {
      throw new Error(`HTTP ${res.status}: ${JSON.stringify(json)}`);
    }

    return json;
  } catch (e) {
    log(`ERROR: ${e.message}`, "error");
    showToast(e.message, "error");
    throw e;
  }
}

// ========================================
// TABS with smooth transitions
// ========================================
document.querySelectorAll(".tab").forEach((btn) => {
  btn.addEventListener("click", () => {
    const tab = btn.dataset.tab;
    
    document.querySelectorAll(".tab").forEach((b) => {
      b.classList.toggle("active", b === btn);
    });
    
    document.querySelectorAll(".tab-content").forEach((c) => {
      const isActive = c.id === `tab-${tab}`;
      c.classList.toggle("active", isActive);
    });
  });
});

// ========================================
// USERS
// ========================================
const usersTbody = document.getElementById("users-table-body");
const usersCount = document.getElementById("users-count");

async function loadUsers() {
  try {
    const users = await apiRequest("/api/users");
    usersTbody.innerHTML = "";
    const usersList = users || [];
    
    if (usersList.length === 0) {
      usersTbody.innerHTML = `
        <tr>
          <td colspan="4" class="empty-state">
            <div class="empty-state-icon">👤</div>
            <div class="empty-state-text">Пользователей пока нет</div>
          </td>
        </tr>
      `;
    } else {
      usersList.forEach((u, index) => {
        const tr = document.createElement("tr");
        tr.style.animationDelay = `${index * 0.05}s`;
        tr.innerHTML = `
          <td><strong>${u.id ?? ""}</strong></td>
          <td>${u.name ?? "—"}</td>
          <td>${u.lastname ?? "—"}</td>
          <td>${u.surname ?? "—"}</td>
        `;
        usersTbody.appendChild(tr);
      });
    }
    
    usersCount.textContent = usersList.length;
  } catch {
    // Error already logged
  }
}

document.getElementById("reload-users").addEventListener("click", () => {
  loadUsers();
  showToast("Список пользователей обновлён", "success");
});

document.getElementById("create-user-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  const form = e.target;
  const data = {
    name: form.name.value.trim(),
    lastname: form.lastname.value.trim() || null,
    surname: form.surname.value.trim() || null,
  };
  
  try {
    await apiRequest("/api/users", {
      method: "POST",
      body: JSON.stringify(data),
    });
    form.reset();
    await loadUsers();
    showToast(`Пользователь ${data.name} создан!`, "success");
  } catch {
    // Error logged
  }
});

// ========================================
// PRODUCTS
// ========================================
const productsTbody = document.getElementById("products-table-body");
const productsCount = document.getElementById("products-count");

async function loadProducts() {
  try {
    const products = await apiRequest("/api/products");
    productsTbody.innerHTML = "";
    const productsList = products || [];
    
    if (productsList.length === 0) {
      productsTbody.innerHTML = `
        <tr>
          <td colspan="3" class="empty-state">
            <div class="empty-state-icon">📦</div>
            <div class="empty-state-text">Продуктов пока нет</div>
          </td>
        </tr>
      `;
    } else {
      productsList.forEach((p, index) => {
        const tr = document.createElement("tr");
        tr.style.animationDelay = `${index * 0.05}s`;
        const priceFormatted = typeof p.price === 'number' 
          ? p.price.toLocaleString('ru-RU', { style: 'currency', currency: 'RUB' })
          : p.price ?? "—";
        tr.innerHTML = `
          <td><strong>${p.id ?? ""}</strong></td>
          <td>${p.name ?? "—"}</td>
          <td class="price">${priceFormatted}</td>
        `;
        productsTbody.appendChild(tr);
      });
    }
    
    productsCount.textContent = productsList.length;
  } catch {
    // Error logged
  }
}

document.getElementById("reload-products").addEventListener("click", () => {
  loadProducts();
  showToast("Список продуктов обновлён", "success");
});

document.getElementById("create-product-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  const form = e.target;
  const data = {
    name: form.name.value.trim(),
    price: parseFloat(form.price.value),
  };
  
  try {
    await apiRequest("/api/products", {
      method: "POST",
      body: JSON.stringify(data),
    });
    form.reset();
    await loadProducts();
    showToast(`Продукт ${data.name} создан!`, "success");
  } catch {
    // Error logged
  }
});

// ========================================
// PETS
// ========================================
const petsTbody = document.getElementById("pets-table-body");
const petsPaginationEl = document.getElementById("pets-pagination");
const petsCount = document.getElementById("pets-count");
let lastPetsQuery = null;

function renderPetsPage(pageData) {
  const content = pageData.content || [];
  petsTbody.innerHTML = "";
  
  if (content.length === 0) {
    petsTbody.innerHTML = `
      <tr>
        <td colspan="3" class="empty-state">
          <div class="empty-state-icon">🐾</div>
          <div class="empty-state-text">Питомцев не найдено</div>
        </td>
      </tr>
    `;
  } else {
    content.forEach((p, index) => {
      const tr = document.createElement("tr");
      tr.style.animationDelay = `${index * 0.05}s`;
      tr.innerHTML = `
        <td><strong>${p.id ?? ""}</strong></td>
        <td>${p.name ?? "—"}</td>
        <td>${p.ownerId ?? "—"}</td>
      `;
      petsTbody.appendChild(tr);
    });
  }

  const page = pageData.number ?? 0;
  const totalPages = pageData.totalPages ?? 1;
  const totalElements = pageData.totalElements ?? content.length;

  petsCount.textContent = totalElements;

  petsPaginationEl.innerHTML = "";
  
  const info = document.createElement("div");
  info.className = "pagination-info";
  info.innerHTML = `
    <span>Страница <strong>${page + 1}</strong> из <strong>${totalPages}</strong></span>
    <span class="separator">•</span>
    <span>Всего: <strong>${totalElements}</strong></span>
  `;

  const controls = document.createElement("div");
  controls.className = "pagination-controls";

  const prevBtn = document.createElement("button");
  prevBtn.className = "btn btn-secondary";
  prevBtn.innerHTML = `
    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
      <path d="M19 12H5M12 19l-7-7 7-7"/>
    </svg>
    Назад
  `;
  prevBtn.disabled = page <= 0;
  prevBtn.addEventListener("click", () => {
    if (lastPetsQuery) {
      loadPetsForUser({ ...lastPetsQuery, page: page - 1 });
    }
  });

  const nextBtn = document.createElement("button");
  nextBtn.className = "btn btn-secondary";
  nextBtn.innerHTML = `
    Вперёд
    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
      <path d="M5 12h14M12 5l7 7-7 7"/>
    </svg>
  `;
  nextBtn.disabled = page + 1 >= totalPages;
  nextBtn.addEventListener("click", () => {
    if (lastPetsQuery) {
      loadPetsForUser({ ...lastPetsQuery, page: page + 1 });
    }
  });

  controls.appendChild(prevBtn);
  controls.appendChild(nextBtn);

  petsPaginationEl.appendChild(info);
  petsPaginationEl.appendChild(controls);
}

async function loadPetsForUser(query) {
  lastPetsQuery = query;
  const params = new URLSearchParams();
  params.set("page", query.page);
  params.set("size", query.size);
  params.set("sortBy", query.sortBy);
  params.set("sortDir", query.sortDir);
  if (query.name) params.set("name", query.name);
  if (query.nameContains) params.set("nameContains", query.nameContains);

  try {
    let url;
    if (query.userId) {
      url = `/api/users/${encodeURIComponent(query.userId)}/pets?${params.toString()}`;
    } else {
      url = `/api/pets?${params.toString()}`;
    }
    const data = await apiRequest(url);
    renderPetsPage(data);
  } catch {
    // Error logged
  }
}

async function loadAllPets() {
  const query = {
    userId: null,
    page: 0,
    size: 10,
    sortBy: "id",
    sortDir: "ASC",
    name: null,
    nameContains: null,
  };
  await loadPetsForUser(query);
}

document.getElementById("load-all-pets").addEventListener("click", () => {
  loadAllPets();
  showToast("Загружаем всех питомцев...", "success");
});

document.getElementById("load-user-pets-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  const form = e.target;
  const userIdValue = form.userId.value.trim();
  const userId = userIdValue ? parseInt(userIdValue, 10) : null;
  
  if (userId !== null && (!userId || userId <= 0)) {
    log("Некорректный ID пользователя", "error");
    showToast("Введите корректный ID пользователя", "error");
    return;
  }
  
  const query = {
    userId,
    page: parseInt(form.page.value || "0", 10),
    size: parseInt(form.size.value || "10", 10),
    sortBy: form.sortBy.value || "id",
    sortDir: form.sortDir.value || "ASC",
    name: form.name.value.trim() || null,
    nameContains: form.nameContains.value.trim() || null,
  };
  
  await loadPetsForUser(query);
});

document.getElementById("create-pet-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  const form = e.target;
  const userId = parseInt(form.userId.value, 10);
  const name = form.name.value.trim();
  
  if (!userId || userId <= 0 || !name) {
    log("Нужно указать ID пользователя и имя питомца", "error");
    showToast("Заполните все обязательные поля", "error");
    return;
  }
  
  const data = { name };
  
  try {
    await apiRequest(`/api/users/${encodeURIComponent(userId)}/pets`, {
      method: "POST",
      body: JSON.stringify(data),
    });
    form.reset();
    showToast(`Питомец ${name} создан!`, "success");
    if (lastPetsQuery && lastPetsQuery.userId === userId) {
      await loadPetsForUser(lastPetsQuery);
    }
  } catch {
    // Error logged
  }
});

// ========================================
// INITIAL LOAD with animation
// ========================================
document.addEventListener("DOMContentLoaded", () => {
  // Add slight delay for visual effect
  setTimeout(() => loadUsers(), 100);
  setTimeout(() => loadProducts(), 200);
  
  // Welcome log
  log("🚀 SA Demo UI инициализирован");
  log("📊 Загрузка данных...");
});
