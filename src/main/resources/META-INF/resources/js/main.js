import { validateTag, validateRepository, validateCommit, monitorFormValidity } from './formValidation.js';
import { clearFormFields, setPageMaxWidth, closeModal } from './utils.js';
import { fetchBuildInfoOnBlur } from './formFill.js';
import { createFormFields } from './formField.js';
import { sendData, openSubmitModal } from './buildInfoSubmission.js';

document.addEventListener('DOMContentLoaded', function () {

    // Initialize all event listeners
    function initEventListeners() {
        document.getElementById('tag').addEventListener('input', validateTag);
        document.getElementById('repository').addEventListener('input', validateRepository);
        document.getElementById('commit').addEventListener('input', validateCommit);

        // Close form submission buttons
        document.querySelectorAll('.close-modal').forEach(button => button.addEventListener('click', closeModal));

        // Open form submission
        document.getElementById('buildForm').addEventListener('submit', openSubmitModal);

        // Send BuildInfo data via POST method
        document.getElementById('send-button').addEventListener('click', sendData);

        // Fetch BuildInfo on tag blur
        fetchBuildInfoOnBlur();
    }

    // Initialization function
    function init() {
        setPageMaxWidth();
        createFormFields();
        clearFormFields();
        monitorFormValidity();
        initEventListeners();
    }

    // Run initialization after DOM content is loaded
    init();

});