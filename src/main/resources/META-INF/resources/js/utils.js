// Adjust the page element's max width
export function setPageMaxWidth() {
    const pageElement = document.querySelector('.pf-v5-c-page');
    if (pageElement) {
        pageElement.style.setProperty('--pf-v5-c-page--section--m-limit-width--MaxWidth', '50rem');
    }
}

export function clearFormFields() {
    // Clear all input fields
    const inputs = document.querySelectorAll('#buildForm input[type="text"]');
    inputs.forEach(input => {
        input.value = '';
    });

    // Uncheck all checkboxes
    const checkboxes = document.querySelectorAll('#buildForm input[type="checkbox"]');
    checkboxes.forEach(checkbox => {
        checkbox.checked = false;
    });

    // Disable the submit button since no checkboxes are selected
    const buildButton = document.getElementById('build-button');
    buildButton.setAttribute('disabled', true);
}

// Utility function to toggle element visibility
export function toggleVisibility(element, shouldShow) {
    if (shouldShow) {
        element.style.visibility = 'visible';
    } else {
        element.style.visibility = 'hidden';
    }
}

// Close the submit modal
export function closeModal() {
    document.getElementById("submit-modal").setAttribute("hidden", true);
}