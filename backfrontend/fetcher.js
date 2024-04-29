function createUserElement(username) {
    const userElement = document.createElement('div');
    userElement.textContent = username;
    document.body.appendChild(userElement);
}


async function fetchAndDisplayUsers() {
    try {
        const response = await fetch('http://endpoint/');
        const data = await response.json();
        data.forEach(user => {
            createUserElement(user.username);
        });
    } catch (error) {
        console.error('Failed to fetch users:', error);
    }
}fetchAndDisplayUsers();


setInterval(() => {
    fetchAndDisplayUsers();  
}, 10000);
