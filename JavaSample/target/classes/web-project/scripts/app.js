// This file contains the main JavaScript logic for the web project.
// It includes DOM manipulation and event listener setup.

document.addEventListener('DOMContentLoaded', () => {
    const greetingElement = document.createElement('h1');
    greetingElement.textContent = 'Hello, World!';
    document.body.appendChild(greetingElement);
});