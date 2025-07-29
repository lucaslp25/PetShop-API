
addEventListener('DOMContentLoaded', ()=>{

    const login_trigger_btn = document.getElementById('login-trigger-btn');
    const close_form_btn = document.getElementById('btn-close-modal');
    const modal_overlay = document.getElementById('modal-overlay');
    const modal_content = document.getElementById('modal-content');

    if (!login_trigger_btn) return;

    function openModal(){
       modal_overlay.classList.add('active');
       modal_content.classList.add('active');
   }

   function closeModal(){
       modal_overlay.classList.remove('active');
       modal_content.classList.remove('active');
   }

   login_trigger_btn.addEventListener('click', openModal);
   close_form_btn.addEventListener('click', closeModal);
   // modal_overlay.addEventListener('click', closeModal);


    const form_ref = document.getElementById('login-form');

    form_ref.addEventListener('submit', async(event) => {

        event.preventDefault();
        console.log('The page not loaded')

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        console.log('Data colected: ', email, password)

        try{
            const response = await fetch('http://localhost:8080/api/v1/auth/login', {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({username:email, password: password}),
            });

            if (response.ok){
                const data = await response.json();

                console.log('Login successfully!', data);

                //save the token on special place in browser
                localStorage.setItem('authToken', data.token);

                const userData = {
                    name: data.name,
                    email: data.email,
                    role: data.role
                }; //the data of my dto

                localStorage.setItem('userData', JSON.stringify(userData));

                updatedUIforLoggedUsers(userData); //the function of the main.js

                alert('Login Successfully!');
                closeModal();
            }else{
                const errorData = await response.json();
                console.error("Login fail: ", errorData)
                alert(`Login Fail: ${errorData.message || 'Email or Password invalid'}`)
            }
        }catch (error){

            console.error("Networking error in login :", error);
            alert("Don´t possible connect to server. Try again later")
        }

    })


})