document.addEventListener('DOMContentLoaded', function () {
	fetch('/challenges.json')
		.then(response => response.json())
		.then(data => {
			const template = document.querySelector('#template');
			const buttonGrid = document.querySelector('#buttonGrid');
			let modalCounter = 1; // starting counter for modal IDs

			data.forEach((challenge, index) => {
				const clone = document.importNode(template.content, true);
				const challengeButton = clone.querySelector('button');
				const modal = clone.querySelector('.micromodal');
				const modalContainer = clone.querySelector('.micromodal__container');
				const form = clone.querySelector('form');
				const challengeNameField = clone.querySelector('input[type="hidden"]');
				const challengeTitle = clone.querySelector('h2');
				const challengeDescription = clone.querySelector('p');

				// assign unique IDs and attributes
				challengeButton.innerText = challenge.c_name;
				challengeButton.setAttribute('data-micromodal-trigger', 'modal-' + modalCounter);
				modal.id = 'modal-' + modalCounter;
				modalContainer.setAttribute('aria-labelledby', 'modal-' + modalCounter + '-title');
				challengeTitle.innerText = challenge.c_name;
				challengeNameField.value = challenge.c_name;
				challengeDescription.innerText = challenge.description;

				buttonGrid.appendChild(clone);
				modalCounter++;
			});

			MicroModal.init();
		});
});