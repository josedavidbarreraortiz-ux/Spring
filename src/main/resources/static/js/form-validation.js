
(function () {
    'use strict';

    // ==================== CONFIGURACIÓN ====================
    const CONFIG = {
        debounceDelay: 300,
        minPasswordLength: 8,
        messages: {
            required: 'Este campo es obligatorio',
            minLength: 'Debe tener al menos {min} caracteres',
            maxLength: 'No puede exceder {max} caracteres',
            email: 'Ingrese un email válido',
            phone: 'Ingrese un teléfono válido (7-15 dígitos)',
            phone10: 'El teléfono debe tener exactamente 10 dígitos',
            address: 'La dirección debe tener más de 5 caracteres y al menos 1 número',
            number: 'Ingrese un número válido',
            positiveNumber: 'Debe ser un número mayor a 0',
            nonNegative: 'No puede ser negativo',
            integer: 'Debe ser un número entero',
            document: 'Documento inválido (5-20 caracteres)',
            barcode: 'Código de barras inválido (8-14 dígitos)',
            select: 'Seleccione una opción',
            stockRange: 'Stock mínimo no puede ser mayor al máximo',
            passwordWeak: 'Contraseña débil',
            passwordMedium: 'Contraseña media',
            passwordStrong: 'Contraseña fuerte'
        }
    };

    // ==================== VALIDADORES ====================
    const Validators = {
        required: (value) => value.trim().length > 0,

        minLength: (value, min) => value.trim().length >= min,

        maxLength: (value, max) => value.trim().length <= max,

        email: (value) => {
            if (!value.trim()) return true; // Se valida con required si es obligatorio
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return emailRegex.test(value.trim());
        },

        phone: (value) => {
            if (!value.trim()) return true;
            const phoneRegex = /^[0-9+\-\s]{7,15}$/;
            return phoneRegex.test(value.replace(/\s/g, ''));
        },

        positiveNumber: (value) => {
            if (!value.trim()) return true;
            const num = parseFloat(value);
            return !isNaN(num) && num > 0;
        },

        nonNegative: (value) => {
            if (!value.trim()) return true;
            const num = parseFloat(value);
            return !isNaN(num) && num >= 0;
        },

        integer: (value) => {
            if (!value.trim()) return true;
            const num = parseInt(value);
            return !isNaN(num) && num.toString() === value.trim();
        },

        document: (value) => {
            if (!value.trim()) return true;
            const docRegex = /^[A-Za-z0-9]{5,20}$/;
            return docRegex.test(value.trim());
        },

        barcode: (value) => {
            if (!value.trim()) return true;
            const barcodeRegex = /^[0-9]{8,14}$/;
            return barcodeRegex.test(value.trim());
        },

        // Teléfono exactamente 10 dígitos
        phone10: (value) => {
            if (!value.trim()) return true;
            const digits = value.replace(/\D/g, '');
            return digits.length === 10;
        },

        // Dirección: más de 5 caracteres y al menos 1 número
        address: (value) => {
            if (!value.trim()) return true;
            const hasMinLength = value.trim().length > 5;
            const hasNumber = /\d/.test(value);
            return hasMinLength && hasNumber;
        },

        // Nombre: solo letras y espacios (no números)
        name: (value) => {
            if (!value.trim()) return true;
            const nameRegex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\s]+$/;
            return nameRegex.test(value.trim());
        },

        // Número de documento: solo números, 5-15 dígitos
        documentNumber: (value) => {
            if (!value.trim()) return true;
            const docRegex = /^[0-9]{5,15}$/;
            return docRegex.test(value.trim());
        },

        select: (value) => value && value !== ''
    };

    // ==================== UTILIDADES ====================
    function debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    function getMessage(key, replacements = {}) {
        let message = CONFIG.messages[key] || key;
        Object.keys(replacements).forEach(k => {
            message = message.replace(`{${k}}`, replacements[k]);
        });
        return message;
    }

    // ==================== UI FEEDBACK ====================
    function setValid(input) {
        input.classList.remove('is-invalid');
        input.classList.add('is-valid');
        const feedback = input.parentElement.querySelector('.invalid-feedback');
        if (feedback) feedback.style.display = 'none';
        const validFeedback = input.parentElement.querySelector('.valid-feedback');
        if (validFeedback) validFeedback.style.display = 'block';
    }

    function setInvalid(input, message) {
        input.classList.remove('is-valid');
        input.classList.add('is-invalid');
        let feedback = input.parentElement.querySelector('.invalid-feedback');
        if (!feedback) {
            feedback = document.createElement('div');
            feedback.className = 'invalid-feedback';
            input.parentElement.appendChild(feedback);
        }
        feedback.textContent = message;
        feedback.style.display = 'block';
        const validFeedback = input.parentElement.querySelector('.valid-feedback');
        if (validFeedback) validFeedback.style.display = 'none';
    }

    function clearValidation(input) {
        input.classList.remove('is-valid', 'is-invalid');
        const feedback = input.parentElement.querySelector('.invalid-feedback');
        if (feedback) feedback.style.display = 'none';
        const validFeedback = input.parentElement.querySelector('.valid-feedback');
        if (validFeedback) validFeedback.style.display = 'none';
    }

    // ==================== PASSWORD STRENGTH ====================
    function getPasswordStrength(password) {
        let strength = 0;
        if (password.length >= 8) strength++;
        if (password.length >= 12) strength++;
        if (/[a-z]/.test(password)) strength++;
        if (/[A-Z]/.test(password)) strength++;
        if (/[0-9]/.test(password)) strength++;
        if (/[^a-zA-Z0-9]/.test(password)) strength++;

        if (strength <= 2) return { level: 'weak', text: CONFIG.messages.passwordWeak, color: '#dc3545' };
        if (strength <= 4) return { level: 'medium', text: CONFIG.messages.passwordMedium, color: '#ffc107' };
        return { level: 'strong', text: CONFIG.messages.passwordStrong, color: '#198754' };
    }

    function updatePasswordStrength(input) {
        const password = input.value;
        let strengthIndicator = input.parentElement.querySelector('.password-strength');

        if (!strengthIndicator) {
            strengthIndicator = document.createElement('div');
            strengthIndicator.className = 'password-strength mt-1';
            strengthIndicator.innerHTML = `
                <div class="progress" style="height: 5px;">
                    <div class="progress-bar" role="progressbar" style="width: 0%"></div>
                </div>
                <small class="strength-text"></small>
            `;
            input.parentElement.appendChild(strengthIndicator);
        }

        if (!password) {
            strengthIndicator.style.display = 'none';
            return;
        }

        strengthIndicator.style.display = 'block';
        const strength = getPasswordStrength(password);
        const progressBar = strengthIndicator.querySelector('.progress-bar');
        const strengthText = strengthIndicator.querySelector('.strength-text');

        const widthMap = { weak: '33%', medium: '66%', strong: '100%' };
        progressBar.style.width = widthMap[strength.level];
        progressBar.style.backgroundColor = strength.color;
        strengthText.textContent = strength.text;
        strengthText.style.color = strength.color;
    }

    // ==================== VALIDACIÓN DE CAMPO ====================
    function validateField(input) {
        const value = input.value;
        const rules = (input.dataset.validate || '').split('|').filter(r => r);
        const isRequired = input.hasAttribute('required') || rules.includes('required');
        const isSelect = input.tagName === 'SELECT';

        // Si está vacío y no es requerido, limpiar validación
        if (!value.trim() && !isRequired) {
            clearValidation(input);
            return true;
        }

        // Para SELECT: validar que tenga una opción seleccionada (valor no vacío)
        if (isSelect) {
            if (isRequired && (!value || value === '')) {
                setInvalid(input, getMessage('select'));
                return false;
            }
            // Si tiene valor, es válido
            if (value && value !== '') {
                setValid(input);
                return true;
            }
        }

        // Validar requerido (para inputs normales)
        if (isRequired && !Validators.required(value)) {
            setInvalid(input, getMessage('required'));
            return false;
        }

        // Validar otras reglas
        for (const rule of rules) {
            if (rule === 'required') continue;

            const [ruleName, param] = rule.split(':');

            switch (ruleName) {
                case 'minLength':
                    if (!Validators.minLength(value, parseInt(param))) {
                        setInvalid(input, getMessage('minLength', { min: param }));
                        return false;
                    }
                    break;
                case 'maxLength':
                    if (!Validators.maxLength(value, parseInt(param))) {
                        setInvalid(input, getMessage('maxLength', { max: param }));
                        return false;
                    }
                    break;
                case 'email':
                    if (!Validators.email(value)) {
                        setInvalid(input, getMessage('email'));
                        return false;
                    }
                    break;
                case 'phone':
                    if (!Validators.phone(value)) {
                        setInvalid(input, getMessage('phone'));
                        return false;
                    }
                    break;
                case 'positiveNumber':
                    if (!Validators.positiveNumber(value)) {
                        setInvalid(input, getMessage('positiveNumber'));
                        return false;
                    }
                    break;
                case 'nonNegative':
                    if (!Validators.nonNegative(value)) {
                        setInvalid(input, getMessage('nonNegative'));
                        return false;
                    }
                    break;
                case 'integer':
                    if (!Validators.integer(value)) {
                        setInvalid(input, getMessage('integer'));
                        return false;
                    }
                    break;
                case 'document':
                    if (!Validators.document(value)) {
                        setInvalid(input, getMessage('document'));
                        return false;
                    }
                    break;
                case 'barcode':
                    if (!Validators.barcode(value)) {
                        setInvalid(input, getMessage('barcode'));
                        return false;
                    }
                    break;
                case 'password':
                    updatePasswordStrength(input);
                    if (value.length < CONFIG.minPasswordLength) {
                        setInvalid(input, getMessage('minLength', { min: CONFIG.minPasswordLength }));
                        return false;
                    }
                    break;
                case 'phone10':
                    if (!Validators.phone10(value)) {
                        setInvalid(input, getMessage('phone10'));
                        return false;
                    }
                    break;
                case 'address':
                    if (!Validators.address(value)) {
                        setInvalid(input, getMessage('address'));
                        return false;
                    }
                    break;
                case 'name':
                    if (!Validators.name(value)) {
                        setInvalid(input, 'El nombre solo puede contener letras y espacios');
                        return false;
                    }
                    break;
                case 'documentNumber':
                    if (!Validators.documentNumber(value)) {
                        setInvalid(input, 'El documento debe tener entre 5 y 15 dígitos numéricos');
                        return false;
                    }
                    break;
            }
        }

        // Si pasó todas las validaciones
        if (value.trim() || isRequired) {
            setValid(input);
        }
        return true;
    }

    // ==================== VALIDACIÓN DE FORMULARIO ====================
    function validateForm(form) {
        let isValid = true;
        const inputs = form.querySelectorAll('input, select, textarea');

        inputs.forEach(input => {
            // Ignorar inputs ocultos y readOnly
            if (input.type === 'hidden' || input.readOnly) return;

            if (!validateField(input)) {
                isValid = false;
            }
        });

        return isValid;
    }

    // ==================== INICIALIZACIÓN ====================
    function initForm(form) {
        if (!form || form.dataset.validationInit) return;
        form.dataset.validationInit = 'true';

        const inputs = form.querySelectorAll('input, select, textarea');

        inputs.forEach(input => {
            // Ignorar inputs ocultos
            if (input.type === 'hidden') return;

            // Crear contenedores de feedback si no existen
            if (!input.parentElement.querySelector('.invalid-feedback')) {
                const invalidFeedback = document.createElement('div');
                invalidFeedback.className = 'invalid-feedback';
                input.parentElement.appendChild(invalidFeedback);
            }

            // Eventos de validación
            const debouncedValidate = debounce(() => validateField(input), CONFIG.debounceDelay);

            input.addEventListener('input', debouncedValidate);
            input.addEventListener('blur', () => validateField(input));
            input.addEventListener('change', () => validateField(input));
        });

        // Interceptar submit
        form.addEventListener('submit', function (e) {
            if (!validateForm(form)) {
                e.preventDefault();
                e.stopPropagation();

                // Scroll al primer error
                const firstError = form.querySelector('.is-invalid');
                if (firstError) {
                    firstError.focus();
                    firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
                }
            }
        });

        // Agregar clase para estilos Bootstrap
        form.classList.add('needs-validation');
    }

    // ==================== AUTO-INICIALIZACIÓN ====================
    function initAllForms() {
        // Inicializar formularios con clase específica
        document.querySelectorAll('form.validate-form, form[data-validate]').forEach(initForm);

        // O inicializar todos los formularios en sección admin
        document.querySelectorAll('.content form').forEach(initForm);
    }

    // Ejecutar cuando el DOM esté listo
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initAllForms);
    } else {
        initAllForms();
    }

    // Exponer API global
    window.FormValidation = {
        init: initForm,
        initAll: initAllForms,
        validate: validateForm,
        validateField: validateField,
        setValid: setValid,
        setInvalid: setInvalid,
        clearValidation: clearValidation
    };

})();
