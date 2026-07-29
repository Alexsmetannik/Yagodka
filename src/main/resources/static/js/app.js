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

// Поля формы
const typeInput = document.getElementById('type');
const displayNameInput = document.getElementById('displayName');
const descriptionInput = document.getElementById('description');
const estimationInput = document.getElementById('estimation');
const imageInput = document.getElementById('image');

// Элементы подсказок
const typeHint = document.getElementById('typeHint');
const displayNameHint = document.getElementById('displayNameHint');
const descriptionHint = document.getElementById('descriptionHint');
const estimationHint = document.getElementById('estimationHint');
const imageHint = document.getElementById('imageHint');
const estimationError = document.getElementById('estimationError');

// Загрузка списка товаров при старте
document.addEventListener('DOMContentLoaded', loadProducts);

// Обработчики событий
addProductBtn.addEventListener('click', () => openAddModal());
cancelBtn.addEventListener('click', closeModal);
cancelDeleteBtn.addEventListener('click', closeDeleteModal);

productForm.addEventListener('submit', handleFormSubmit);

// Валидация полей в реальном времени
typeInput.addEventListener('input', validateField);
displayNameInput.addEventListener('input', validateField);
descriptionInput.addEventListener('input', validateField);
estimationInput.addEventListener('input', validateField);
imageInput.addEventListener('input', validateField);

// Удаление товара
confirmDeleteBtn.addEventListener('click', handleDeleteConfirm);

// Закрытие модального окна по клику вне
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

// Закрытие по клавише Escape
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

// Функция валидации отдельного поля
function validateField(e) {
    const field = e.target;
    const fieldName = field.id;
    let isValid = true;
    let hintElement = null;
    let maxLength = 0;

    // Определяем максимальную длину и элемент подсказки
    switch(fieldName) {
        case 'type':
            hintElement = typeHint;
            maxLength = 20;
            break;
        case 'displayName':
            hintElement = displayNameHint;
            maxLength = 20;
            break;
        case 'description':
            hintElement = descriptionHint;
            maxLength = 200;
            break;
        case 'image':
            hintElement = imageHint;
            maxLength = 50;
            break;
        case 'estimation':
            hintElement = estimationHint;
            break;
        default:
            return;
    }

    // Валидация для текстовых полей
    if (fieldName !== 'estimation') {
        const value = field.value;
        if (value.length > maxLength) {
            field.classList.add('error');
            hintElement.classList.add('error');
            isValid = false;
        } else {
            field.classList.remove('error');
            hintElement.classList.remove('error');
        }
    }

    // Валидация для оценки
    if (fieldName === 'estimation') {
        const value = field.value;
        const intValue = parseInt(value);

        if (value === '' || value === null || value === undefined) {
            field.classList.remove('error');
            estimationError.classList.add('hidden');
            isValid = true;
        } else if (!/^-?\d+$/.test(value) || intValue < 0 || intValue > 10) {
            field.classList.add('error');
            estimationError.classList.remove('hidden');
            isValid = false;
        } else {
            field.classList.remove('error');
            estimationError.classList.add('hidden');
        }
    }

    // Обновляем состояние кнопки
    validateForm();
}

// Валидация всей формы
function validateForm() {
    const type = typeInput.value.trim();
    const displayName = displayNameInput.value.trim();
    const description = descriptionInput.value.trim();
    const estimation = estimationInput.value;
    const image = imageInput.value.trim();

    // Проверка на пустые значения
    const isFilled = type !== '' &&
        displayName !== '' &&
        description !== '' &&
        estimation !== '' &&
        image !== '';

    // Проверка на превышение длины
    const isTypeValid = type.length <= 20;
    const isDisplayNameValid = displayName.length <= 20;
    const isDescriptionValid = description.length <= 200;
    const isImageValid = image.length <= 50;

    // Проверка оценки
    const isEstimationValid = validateEstimation();

    // Проверка на наличие ошибок в полях
    const hasErrors = document.querySelectorAll('.form-group input.error, .form-group textarea.error').length > 0;

    const isValid = isFilled &&
        isTypeValid &&
        isDisplayNameValid &&
        isDescriptionValid &&
        isImageValid &&
        isEstimationValid &&
        !hasErrors;

    submitBtn.disabled = !isValid;

    if (isValid) {
        submitBtn.classList.add('active');
    } else {
        submitBtn.classList.remove('active');
    }

    return isValid;
}

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

// Функция рендеринга звездочек
function renderStars(estimation) {
    let starsHtml = '<div class="estimation-container">';
    starsHtml += '<span class="stars">';
    for (let i = 0; i < 10; i++) {
        if (i < estimation) {
            starsHtml += '<span class="star filled">★</span>';
        } else {
            starsHtml += '<span class="star">★</span>';
        }
    }
    starsHtml += '</span>';
    starsHtml += `<span class="estimation-text">(${estimation}/10)</span>`;
    starsHtml += '</div>';
    return starsHtml;
}

// Функция обрезки описания
function truncateDescription(description, maxLength = 50) {
    if (!description) return '';
    if (description.length <= maxLength) return description;
    return description.substring(0, maxLength) + '...';
}

// Функция рендеринга товаров
function renderProducts(products) {
    if (!products || products.length === 0) {
        productList.innerHTML = '<div class="empty-message">Список товаров пуст</div>';
        return;
    }

    productList.innerHTML = products.map(product => `
        <div class="product-item" data-id="${product.id}">
            <div class="product-image-wrapper">
                <img src="${product.image || 'https://via.placeholder.com/400x400?text=Нет+фото'}" 
                     alt="${product.displayName}" 
                     class="product-image"
                     onerror="this.src='https://via.placeholder.com/400x400?text=Нет+фото'">
            </div>
            <div class="product-type">Тип товара: ${product.type}</div>
            <div class="product-name">${product.displayName}</div>
            <div class="product-description">${truncateDescription(product.description)}</div>
            <div class="product-estimation">
                ${renderStars(product.estimation)}
            </div>
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

        typeInput.value = product.type || '';
        displayNameInput.value = product.displayName || '';
        descriptionInput.value = product.description || '';
        estimationInput.value = product.estimation || '';
        imageInput.value = product.image || '';

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
        type: typeInput.value.trim(),
        displayName: displayNameInput.value.trim(),
        description: descriptionInput.value.trim(),
        estimation: parseInt(estimationInput.value),
        image: imageInput.value.trim()
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
            await loadProducts();
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
            await loadProducts();
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

// Очистка ошибок
function clearErrors() {
    // Очищаем ошибки всех полей
    const fields = [typeInput, displayNameInput, descriptionInput, imageInput];
    const hints = [typeHint, displayNameHint, descriptionHint, imageHint];

    fields.forEach(field => {
        field.classList.remove('error');
    });

    hints.forEach(hint => {
        hint.classList.remove('error');
    });

    estimationError.classList.add('hidden');
    estimationInput.classList.remove('error');
}