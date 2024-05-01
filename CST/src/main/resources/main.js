document.addEventListener('DOMContentLoaded', function () {
	fetch('/challenges.json')
		.then(response => response.json())
		.then(data => {
			const template = document.querySelector('#template');
			const buttonGrid = document.querySelector('#buttonGrid');
			let modalCounter = 1; // starting counter for modal IDs

			// Fetch the completed flags
			fetch('/get_flags')
				.then(flagsResponse => flagsResponse.json())
				.then(flagsData => {
					const completedFlags = new Set(flagsData); // Store completed flags in a Set for fast access

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

						// Check if the challenge has been completed and change button color
						if (completedFlags.has(challenge.c_name)) {
							challengeButton.style.backgroundColor = 'green';
						}

						buttonGrid.appendChild(clone);
						modalCounter++;
					});

					MicroModal.init();
				})
				.catch(error => {
					console.error('Error fetching completed flags:', error);
					// Proceed with the normal process if JSON is invalid or fetch fails
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
});
