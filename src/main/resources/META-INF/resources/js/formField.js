function formFieldTemplate(id, labelText, placeholder, alertMessage) {
    // Construct the HTML for the form field using template literals
    const formFieldHtml = `
        <label class="pf-v5-c-form__label" for="${id}">
            <span class="pf-v5-c-form__label-text">${labelText}</span>&nbsp;<span class="pf-v5-c-form__label-required" aria-hidden="true">&#42;</span>
        </label>
        <div class="pf-v5-c-form__group-label">
            <div class="pf-v5-c-form-control pf-m-required">
                <input type="text" id="${id}" class="form-field" placeholder="${placeholder}" required>
            </div>
            <div class="pf-v5-c-form__helper-text" id="${id}-alert" style="visibility: hidden;">
                <div class="pf-v5-c-helper-text">
                    <div class="pf-v5-c-helper-text__item pf-m-error">
                        <span class="pf-v5-c-helper-text__item-text">${alertMessage}</span>
                    </div>
                </div>
            </div>
        </div>
    `;

    // Create a container element to hold the form field HTML
    const fieldContainer = document.createElement('div');
    fieldContainer.classList.add('pf-v5-c-form__group'); // Add class to match PF form structure
    fieldContainer.innerHTML = formFieldHtml;

    document.getElementById("form-fields").appendChild(fieldContainer);
}

export function createFormFields() {
    formFieldTemplate('tag', 'Tag Link', 'e.g., https://github.com/wildfly-security/wildfly-elytron/releases/tag/2.5.2.Final', 'The tag link is not in a valid URL format.');
    formFieldTemplate('repository', 'Repository', 'e.g., https://github.com/wildfly-security/wildfly-elytron', 'The repository link is not in a valid URL format.');
    formFieldTemplate('version', 'Tag Version', 'e.g., 2.5.2.Final', 'Invisible placeholder to keep the layout consistent');
    formFieldTemplate('commit', 'Commit SHA', 'e.g., 93995758e8ee1a3380e397057b28ef12bf505335', 'Invalid commit SHA. A valid commit SHA is a 7 to 40 character hexadecimal string.');
}

