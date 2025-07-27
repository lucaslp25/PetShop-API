const login_trigger_btn = document.getElementById('login-trigger-btn');
const close_form_btn = document.getElementById('btn-close-modal');
const modal_overlay = document.getElementById('modal-overlay');
const modal_content = document.getElementById('modal-content');


addEventListener('DOMContentLoaded', ()=>{

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


})