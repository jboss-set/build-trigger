import { toggleVisibility } from './utils.js';  

const httpsPattern = /^https:\/\/.+$/;

// Function to check if any alert divs are visible
export function checkIfFormIsValid() {
    const alertDivs = document.querySelectorAll('[id*="-alert"]');
    const hasVisibleAlert = Array.from(alertDivs).some(alertDiv => getComputedStyle(alertDiv).visibility === 'visible');

    const buildButton = document.getElementById('build-button');
    if (hasVisibleAlert || !isOneCheckboxSelected() || !allFormFieldsFilled()) {
        buildButton.setAttribute('disabled', true);
    } else {
        buildButton.removeAttribute('disabled');
    }
}

// Set up MutationObserver to monitor changes in alert visibility
export function monitorFormValidity() {
    const alertDivs = document.querySelectorAll('[id*="-alert"]');
    alertDivs.forEach(alertDiv => {
        const observer = new MutationObserver(checkIfFormIsValid);
        observer.observe(alertDiv, { attributes: true });
    });

    // Trigger form validity checks on form fields
    document.querySelectorAll('.form-field').forEach(field => {
        field.addEventListener('input', checkIfFormIsValid);
    });

    // Listen for changes in the stream checkboxes
    document.querySelectorAll('input[name="streams"]').forEach(checkbox => {
        checkbox.addEventListener('change', validateStreams);
    });
}

// Validate the "tag" input field
export function validateTag() {
    const inputValue = this.value.trim();
    const alertMessage = document.getElementById('tag-alert');
    toggleVisibility(alertMessage, inputValue && !httpsPattern.test(inputValue));
}

// Validate the "repository" input field with https pattern
export function validateRepository() {
    const inputValue = this.value.trim();
    const alertMessage = document.getElementById('repository-alert');
    toggleVisibility(alertMessage, inputValue && !httpsPattern.test(inputValue));
}

// Validate the "commit" input field. A valid commit SHA is a 7 to 40 character hexadecimal string.
export function validateCommit() {
    const inputValue = this.value.trim();
    const alertMessage = document.getElementById('commit-alert');
    // Check if the input is a valid hexadecimal string and has the correct length
    const isValidSHA = /^[a-fA-F0-9]{7,40}$/.test(inputValue);
    toggleVisibility(alertMessage, inputValue && !isValidSHA);
}

// Check if at least one checkbox is selected in the "streams" group
function isOneCheckboxSelected() {
    return document.querySelectorAll('input[name="streams"]:checked').length > 0;
}

// Check if all form fields are filed.
function allFormFieldsFilled() {
    return Array.from(document.querySelectorAll('.form-field'))
        .every(field => field.value.trim() !== '');
}
// Validate if any streams checkboxes are selected
export function validateStreams() {
    const streamAlert = document.getElementById("stream-alert");
    toggleVisibility(streamAlert, !isOneCheckboxSelected());
    // Check alerts so the submit button is correctly enabled/disabled.
    checkIfFormIsValid();
}