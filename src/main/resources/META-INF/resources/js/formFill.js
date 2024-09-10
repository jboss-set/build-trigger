import { showSuccessToast, showDangerToast, showTime7s, showTime10s } from "./toastNotifications.js";
import { checkIfFormIsValid } from "./formValidation.js";

function setInputValueAndTriggerEvent(element, value) {
    if (value) {
        element.value = value;
        element.dispatchEvent(new Event('input')); // Dispatch 'input' event
    }
}

function populateBuildInfo(buildInfo) {
    setInputValueAndTriggerEvent(document.getElementById("repository"), buildInfo.gitRepo);
    setInputValueAndTriggerEvent(document.getElementById("version"), buildInfo.projectVersion);
    setInputValueAndTriggerEvent(document.getElementById("commit"), buildInfo.commitSha);
}

// Fetch BuildInfo when tag input loses focus
export function fetchBuildInfoOnBlur() {
    document.getElementById('tag').addEventListener('blur', function () {
        const tag = this.value;
        if (tag) {
            const fetchUrl = "/build-trigger/getBuildInfo?tag=" + encodeURIComponent(tag);

            fetch(fetchUrl)
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Failed to fetch build info.");
                    }
                    return response.json();
                })
                .then(data => {
                    console.log("BuildInfo response:", data);
                    populateBuildInfo(data);
                    checkIfFormIsValid();
                    showSuccessToast("Form successfuly auto-filled.", showTime7s);
                })
                .catch(error => {
                    console.log("Error fetching build info:", error);
                    showDangerToast(
                        '<p>Could not fetch the build data for the specified tag.</p>' +
                        '<p>Fetching data is only supported from <b>github.com</b>, <b>gitlab.com</b>, or <b>gitlab.cee.redhat.com</b>.</p>' +
                        '<p>Please enter the data manually or try again.</p>'
                    , showTime10s);                
                });
        }
    });
}