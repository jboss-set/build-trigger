import { showSuccessToast, showDangerToast, showTime7s } from './toastNotifications.js';
import { clearFormFields, closeModal } from './utils.js';

export function openSubmitModal(event) {
    event.preventDefault();

    // Display data directly in modal
    const modalBody = document.getElementById("modal-body");
    modalBody.innerHTML = `
        <p><strong>Public Tag:</strong> ${document.getElementById("tag").value}</p>
        <p><strong>Repository:</strong> ${document.getElementById("repository").value}</p>
        <p><strong>Version:</strong> ${document.getElementById("version").value}</p>
        <p><strong>Commit SHA:</strong> ${document.getElementById("commit").value}</p>
        <p><strong>Streams:</strong> ${Array.from(document.querySelectorAll('input[name="streams"]:checked'))
            .map(el => el.labels[0].innerText).join(', ')}</p>
    `;


    // Show the modal with the BuildInfo
    document.getElementById("submit-modal").removeAttribute("hidden");
}

// Send form data when send button is clicked
export function sendData() {
    const buildInfo = {
        tag: document.getElementById("tag").value,
        gitRepo: document.getElementById("repository").value,
        projectVersion: document.getElementById("version").value,
        commitSha: document.getElementById("commit").value,
        streams: Array.from(document.querySelectorAll('input[name="streams"]:checked')).map(el => el.labels[0].innerText)
    };

    const post_url = "/build-trigger/trigger";

    fetch(post_url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(buildInfo)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return response.text()
    })
    .then(data => {
        console.log(data)
        showSuccessToast(data, showTime7s);
    })
    .catch(error => {
        console.log("Error occurred:", error);
        showDangerToast(`Error message: <b>${error.message}</b>`, showTime7s);
    });

    console.log('Build Info:', buildInfo);
    clearFormFields(); 
    closeModal();
}