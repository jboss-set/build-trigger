export const showTime10s = 10000;
export const showTime7s = 7000;

function showToast(message, alertClass, iconClass, title, showTime) {
    // Create a new alert div
    const alertGroup = document.getElementById('toast-container');

    const alertDiv = document.createElement('div');
    alertDiv.classList.add('pf-v5-c-alert-group__item');

    // Construct the alert HTML using string concatenation and template literals
    const alertHtml = `
        <div class="pf-v5-c-alert ${alertClass}">
            <div class="pf-v5-c-alert__icon">
                <i class="fas fa-fw ${iconClass}"></i>
            </div>
            <p class="pf-v5-c-alert__title">${title}</p>
            <div class="pf-v5-c-alert__description">
                <p>${message}</p>
            </div>
        </div>
    `;

    alertDiv.innerHTML = alertHtml;
    alertGroup.appendChild(alertDiv);

    // Remove the alert after chosen time in miliseconds
    setTimeout(() => {
        alertDiv.remove();
    }, showTime);
}

export function showSuccessToast(message, showTime) {
    const alertClass = 'pf-m-success';
    const iconClass = 'fa-check-circle';
    const title = 'Operation sucessfull.';
    showToast(message, alertClass, iconClass, title, showTime);
}

export function showDangerToast(message, showTime) {
    const alertClass = 'pf-m-danger';
    const iconClass = 'fa-exclamation-circle';
    const title = 'An error occurred.';
    showToast(message, alertClass, iconClass, title, showTime);
}