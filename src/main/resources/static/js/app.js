// Базовый URL для API
const API_BASE_URL = 'http://localhost:8080/v1/public/entity';

// Состояние приложения
let currentProductId = null;
let isEditMode = false;

// DOM элементы
const productList = document.getElementById('productList');
const addProductBtn = document.getElementById('addProductBtn');
const modal = document.getElementById('modal');
const deleteModal = document.getElementById('deleteModal');
const modalTitle = document.getElementById('modalTitle');
const productForm = document.getElementById('productForm');
const submitBtn = document.getElementById('submitBtn');
const cancelBtn = document.getElementById('cancelBtn');
const cancelDeleteBtn = document.getElementById('cancelDeleteBtn');
const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');
const estimationInput = document.getElementById('estimation');
const estimationError = document.getElementById('estimationError');

// Загрузка списка товаров при старте
document.addEventListener('DOMContentLoaded', loadProducts);

// Обработчики событий
addProductBtn.addEventListener('click', () => openAddModal());
cancelBtn.addEventListener('click', closeModal);
cancelDeleteBtn.addEventListener('click', closeDeleteModal);

productForm.addEventListener('submit', handleFormSubmit);

// Валидация оценки в реальном времени
estimationInput.addEventListener('input', validateEstimation);

// Валидация всех полей формы
productForm.addEventListener('input', validateForm);

// Удаление товара
confirmDeleteBtn.addEventListener('click', handleDeleteConfirm);

// Функция загрузки товаров
async function loadProducts() {
    try {
        const response = await fetch(API_BASE_URL + '/list');
        const data = await response.json();

        if (data.status === 'success') {
            renderProducts(data.entities || []);
        } else {
            console.error('Ошибка загрузки товаров:', data.errors);
            renderProducts([]);
        }
    } catch (error) {
        console.error('Ошибка при загрузке товаров:', error);
        renderProducts([]);
    }
}

// Функция рендеринга товаров
function renderProducts(products) {
    if (!products || products.length === 0) {
        productList.innerHTML = '<div class="empty-message">Список товаров пуст</div>';
        return;
    }

    productList.innerHTML = products.map(product => `
        <div class="product-item" data-id="${product.id}">
            <img src="${product.image || 'https://via.placeholder.com/300x200?text=Нет+фото'}" 
                 alt="${product.displayName}" 
                 class="product-image"
                 onerror="this.src='https://via.placeholder.com/300x200?text=Нет+фото'">
            <div class="product-type">Тип: ${product.type}</div>
            <div class="product-name">${product.displayName}</div>
            <div class="product-description">${product.description}</div>
            <div class="product-estimation">Оценка: ${'★'.repeat(product.estimation)}${'☆'.repeat(10 - product.estimation)} (${product.estimation}/10)</div>
            <div class="product-actions">
                <button class="btn btn-secondary" onclick="openEditModal(${product.id})">Редактировать</button>
                <button class="btn btn-secondary" onclick="openDeleteModal(${product.id})">Удалить</button>
            </div>
        </div>
    `).join('');
}

// Открытие модального окна добавления
function openAddModal() {
    isEditMode = false;
    currentProductId = null;
    modalTitle.textContent = 'Добавление товара';
    submitBtn.textContent = 'Добавить';
    submitBtn.classList.remove('active');
    productForm.reset();
    clearErrors();
    submitBtn.disabled = true;
    modal.classList.remove('hidden');
}

// Открытие модального окна редактирования
async function openEditModal(id) {
    try {
        const response = await fetch(API_BASE_URL + '/list');
        const data = await response.json();
        const product = data.entities.find(p => p.id === id);

        if (!product) {
            alert('Товар не найден');
            return;
        }

        isEditMode = true;
        currentProductId = id;
        modalTitle.textContent = 'Изменение товара';
        submitBtn.textContent = 'Изменить';
        submitBtn.classList.remove('active');

        document.getElementById('type').value = product.type || '';
        document.getElementById('displayName').value = product.displayName || '';
        document.getElementById('description').value = product.description || '';
        document.getElementById('estimation').value = product.estimation || '';
        document.getElementById('image').value = product.image || '';

        clearErrors();
        validateForm();
        modal.classList.remove('hidden');
    } catch (error) {
        console.error('Ошибка загрузки товара для редактирования:', error);
        alert('Ошибка загрузки данных товара');
    }
}

// Открытие модального окна удаления
function openDeleteModal(id) {
    currentProductId = id;
    deleteModal.classList.remove('hidden');
}

// Закрытие модального окна формы
function closeModal() {
    modal.classList.add('hidden');
    productForm.reset();
    clearErrors();
    submitBtn.disabled = true;
    submitBtn.classList.remove('active');
    isEditMode = false;
    currentProductId = null;
}

// Закрытие модального окна удаления
function closeDeleteModal() {
    deleteModal.classList.add('hidden');
    currentProductId = null;
}

// Обработка отправки формы
async function handleFormSubmit(e) {
    e.preventDefault();

    if (!validateForm()) {
        return;
    }

    const formData = {
        type: document.getElementById('type').value.trim(),
        displayName: document.getElementById('displayName').value.trim(),
        description: document.getElementById('description').value.trim(),
        estimation: parseInt(document.getElementById('estimation').value),
        image: document.getElementById('image').value.trim()
    };

    try {
        let response;
        let url = API_BASE_URL;
        let method = 'POST';

        if (isEditMode && currentProductId) {
            url = `${API_BASE_URL}/${currentProductId}`;
            method = 'PUT';
        }

        response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        const result = await response.json();

        if (response.ok && result.status === 'success') {
            closeModal();
            await loadProducts(); // Обновляем список
        } else {
            alert('Ошибка: ' + (result.errors ? result.errors.join(', ') : 'Не удалось сохранить товар'));
        }
    } catch (error) {
        console.error('Ошибка при сохранении товара:', error);
        alert('Произошла ошибка при сохранении товара');
    }
}

// Обработка подтверждения удаления
async function handleDeleteConfirm() {
    if (!currentProductId) return;

    try {
        const response = await fetch(`${API_BASE_URL}/${currentProductId}`, {
            method: 'DELETE'
        });

        const result = await response.json();

        if (response.ok && result.status === 'success') {
            closeDeleteModal();
            await loadProducts(); // Обновляем список
        } else {
            alert('Ошибка: ' + (result.errors ? result.errors.join(', ') : 'Не удалось удалить товар'));
        }
    } catch (error) {
        console.error('Ошибка при удалении товара:', error);
        alert('Произошла ошибка при удалении товара');
    }
}

// Валидация оценки
function validateEstimation() {
    const value = estimationInput.value;
    const intValue = parseInt(value);

    if (value === '' || value === null || value === undefined) {
        estimationError.classList.add('hidden');
        return true;
    }

    if (!/^-?\d+$/.test(value) || intValue < 0 || intValue > 10) {
        estimationError.classList.remove('hidden');
        return false;
    } else {
        estimationError.classList.add('hidden');
        return true;
    }
}

// Валидация всей формы
function validateForm() {
    const type = document.getElementById('type').value.trim();
    const displayName = document.getElementById('displayName').value.trim();
    const description = document.getElementById('description').value.trim();
    const estimation = document.getElementById('estimation').value;
    const image = document.getElementById('image').value.trim();

    const isEstimationValid = validateEstimation();

    const isValid = type !== '' &&
        displayName !== '' &&
        description !== '' &&
        estimation !== '' &&
        image !== '' &&
        isEstimationValid;

    submitBtn.disabled = !isValid;

    if (isValid) {
        submitBtn.classList.add('active');
    } else {
        submitBtn.classList.remove('active');
    }

    return isValid;
}

// Очистка ошибок
function clearErrors() {
    estimationError.classList.add('hidden');
    document.querySelectorAll('.form-group input, .form-group textarea').forEach(el => {
        el.classList.remove('invalid');
    });
}

// Закрытие модального окна по клику вне его
modal.addEventListener('click', (e) => {
    if (e.target === modal) {
        closeModal();
    }
});

deleteModal.addEventListener('click', (e) => {
    if (e.target === deleteModal) {
        closeDeleteModal();
    }
});

// Обработка клавиши Escape
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
        if (!modal.classList.contains('hidden')) {
            closeModal();
        }
        if (!deleteModal.classList.contains('hidden')) {
            closeDeleteModal();
        }
    }
});