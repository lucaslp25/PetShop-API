addEventListener('DOMContentLoaded', ()=> {
    stateLoginVerify();
});

    function stateLoginVerify(){

        const userDataString = localStorage.getItem('userData');

        if (userDataString){

            //if find, transform the string for object
            const userData = JSON.parse(userDataString);

            //and call a function for change de UI[
            updatedUIforLoggedUsers(userData);
        }else{
            console.log('No user logged in')
            document.getElementById('login-trigger-btn').style.display = 'block';
        }

    }



function updatedUIforLoggedUsers(user) {
    const loginButton = document.getElementById('login-trigger-btn');
    if(loginButton) {
        loginButton.style.display = 'none'; //hidden the enter button
    }

    const headerContent = document.querySelector('.header-content'); //the place of salutation

    if (!document.getElementById('user-salutation')) {
        const salutationDiv = document.createElement('div');
        salutationDiv.id = 'user-salutation'; // given id for control
        salutationDiv.innerHTML = `
            <span class="salutation-span">Hello, ${user.name}!</span>
            <button id="logout-btn" class="btn-login">Sair</button>
        `;
        headerContent.appendChild(salutationDiv);

        //logout functionalities
        document.getElementById('logout-btn').addEventListener('click', () => { //when click in button
            localStorage.removeItem('authToken'); //remove the token and the users info of localStorage
            localStorage.removeItem('userData');
            window.location.reload(); // reload the page for initial state
        });
    }

    if (user.role === 'employee' || user.role === 'ROLE_EMPLOYEE') {
        console.log("User is a employee. Showing admin options.");
        document.querySelectorAll('.admin-only').forEach(elem => {
            elem.style.display = 'block'; // Or 'list-item', etc.
        });
    }


}