// Simple helper for logging to side panel
const logEl = document.getElementById("log-output");

function log(message, type = "info") {
  const line = document.createElement("div");
  line.className = `log-line${type === "error" ? " log-line--error" : ""}`;
  const time = new Date().toLocaleTimeString();
  line.innerHTML = `<time>[${time}]</time> <code>${message}</code>`;
  logEl.prepend(line);
}

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

    log(`${res.status} ${res.statusText} <- ${url}`);

    if (!res.ok) {
      throw new Error(`HTTP ${res.status}: ${JSON.stringify(json)}`);
    }

    return json;
  } catch (e) {
    log(`ERROR: ${e.message}`, "error");
    throw e;
  }
}

// Tabs
document.querySelectorAll(".tab").forEach((btn) => {
  btn.addEventListener("click", () => {
    const tab = btn.dataset.tab;
    document
      .querySelectorAll(".tab")
      .forEach((b) => b.classList.toggle("active", b === btn));
    document
      .querySelectorAll(".tab-content")
      .forEach((c) => c.classList.toggle("active", c.id === `tab-${tab}`));
  });
});

// USERS
const usersTbody = document.getElementById("users-table-body");

async function loadUsers() {
  try {
    const users = await apiRequest("/api/users");
    usersTbody.innerHTML = "";
    (users || []).forEach((u) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${u.id ?? ""}</td>
        <td>${u.name ?? ""}</td>
        <td>${u.lastname ?? ""}</td>
        <td>${u.surname ?? ""}</td>
      `;
      usersTbody.appendChild(tr);
    });
  } catch {
    // already logged
  }
}

document
  .getElementById("reload-users")
  .addEventListener("click", () => loadUsers());

document
  .getElementById("create-user-form")
  .addEventListener("submit", async (e) => {
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
    } catch {
      // logged
    }
  });

// PRODUCTS
const productsTbody = document.getElementById("products-table-body");

async function loadProducts() {
  try {
    const products = await apiRequest("/api/products");
    productsTbody.innerHTML = "";
    (products || []).forEach((p) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${p.id ?? ""}</td>
        <td>${p.name ?? ""}</td>
        <td>${p.price ?? ""}</td>
      `;
      productsTbody.appendChild(tr);
    });
  } catch {
    // logged
  }
}

document
  .getElementById("reload-products")
  .addEventListener("click", () => loadProducts());

document
  .getElementById("create-product-form")
  .addEventListener("submit", async (e) => {
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
    } catch {
      // logged
    }
  });

// PETS
const petsTbody = document.getElementById("pets-table-body");
const petsPaginationEl = document.getElementById("pets-pagination");
let lastPetsQuery = null;

function renderPetsPage(pageData) {
  const content = pageData.content || [];
  petsTbody.innerHTML = "";
  content.forEach((p) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${p.id ?? ""}</td>
      <td>${p.name ?? ""}</td>
      <td>${p.ownerId ?? ""}</td>
    `;
    petsTbody.appendChild(tr);
  });

  const page = pageData.number ?? 0;
  const totalPages = pageData.totalPages ?? 1;
  const totalElements = pageData.totalElements ?? content.length;

  petsPaginationEl.innerHTML = "";
  const info = document.createElement("div");
  info.textContent = `Страница ${page + 1} из ${totalPages}, всего ${totalElements}`;

  const controls = document.createElement("div");
  controls.className = "pagination-controls";

  const prevBtn = document.createElement("button");
  prevBtn.className = "btn btn-secondary";
  prevBtn.textContent = "⟵ Назад";
  prevBtn.disabled = page <= 0;
  prevBtn.addEventListener("click", () => {
    if (lastPetsQuery) {
      loadPetsForUser({ ...lastPetsQuery, page: page - 1 });
    }
  });

  const nextBtn = document.createElement("button");
  nextBtn.className = "btn btn-secondary";
  nextBtn.textContent = "Вперед ⟶";
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
    // logged
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

document
  .getElementById("load-all-pets")
  .addEventListener("click", () => loadAllPets());

document
  .getElementById("load-user-pets-form")
  .addEventListener("submit", async (e) => {
    e.preventDefault();
    const form = e.target;
    const userIdValue = form.userId.value.trim();
    const userId = userIdValue ? parseInt(userIdValue, 10) : null;
    if (userId !== null && (!userId || userId <= 0)) {
      log("Некорректный ID пользователя", "error");
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

document
  .getElementById("create-pet-form")
  .addEventListener("submit", async (e) => {
    e.preventDefault();
    const form = e.target;
    const userId = parseInt(form.userId.value, 10);
    const name = form.name.value.trim();
    if (!userId || userId <= 0 || !name) {
      log("Нужно указать ID пользователя и имя питомца", "error");
      return;
    }
    const data = { name };
    try {
      await apiRequest(`/api/users/${encodeURIComponent(userId)}/pets`, {
        method: "POST",
        body: JSON.stringify(data),
      });
      form.reset();
      if (lastPetsQuery && lastPetsQuery.userId === userId) {
        await loadPetsForUser(lastPetsQuery);
      }
    } catch {
      // logged
    }
  });

// initial load
loadUsers();
loadProducts();

