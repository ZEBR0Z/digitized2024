function createUserElement(name, score) {
    const userElement = document.createElement('div');
    userElement.textContent = `${name}: ${score}`;
    document.body.appendChild(userElement);
}


async function fetchAndDisplayUsers(existingUsers) {
    try {
        const response = await fetch('http://endpoint/');
        const users = await response.json();
        users.forEach(user => {
            if (!existingUsers.includes(user.name)) {
                createUserElement(user.name, user.score);
                existingUsers.push(user.name); 
            }
        });
    } catch (error) {
        console.error('Failed to fetch users:', error);
    }
}

// Initial setup
const existingUsers = []; // To keep track of displayed users
fetchAndDisplayUsers(existingUsers);

setInterval(() => {
    fetchAndDisplayUsers(existingUsers);
}, 10000);
