addEventListener('DOMContentLoaded', async ()=> {

    await fetchAllProductsOnce();

    stateLoginVerify();
    showCartProducts();
});

let cart = [];
let allProducts = [];

async function fetchAllProductsOnce(){

    try{
        const response = await fetch("http://localhost:8080/api/v1/products");
        if (!response.ok) {
            throw new Error('Failed to fetch products');
        }
        const apiProducts = await response.json();

        allProducts = apiProducts; //pu the products in the global list.
        console.log("Products loaded to store array.", allProducts)

    }catch(error){
        console.log('Error in fetch initial products: ', error)
    }

}

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




function addProductToCart(id, quantity){

    const productIdNum = parseInt(id, 10);
    const existentItem = cart.find(item => item.id === productIdNum && item.type === 'product');


    if (existentItem){
        existentItem.quantity += quantity;
    }else{
        cart.push({ id: productIdNum, quantity: quantity, type: 'product'})
    }

    console.log('current cart:', cart)
    alert(`Product ${id} successfully added in cart!`);
    updateCartUI();
}

function updateCartUI(){
    renderCartItems();
    updateCartCounter();
    updateCartTotal();
}

function updateCartCounter(){

    const counter = document.getElementById('cart-counter');
    const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
    counter.textContent = totalItems;

}

function updateCartTotal(){

    const totalElement = document.getElementById('cart-total');
    const total = cart.reduce((sum, item) => {
        const productDetails = allProducts.find(p => p.id == item.id);
        if (productDetails){
            return sum + (productDetails.price * item.quantity);
        }
        return sum;
    }, 0);
    totalElement.textContent = `R$ ${total.toFixed(2)}`
}


function showCartProducts(){

    const cartBtn = document.getElementById('my-cart-items');
    const closeCartItemsBtn = document.getElementById('btn-close-modal-cart');

    const modalContentCart = document.getElementById('modal-content-cart');


    cartBtn.addEventListener('click',  (event) => {

        const context = document.getElementById('modal-overlay-cart');
        context.classList.add('active');
        modalContentCart.classList.add('active');
    });

    closeCartItemsBtn.addEventListener('click', (event) =>{

        const close = document.getElementById('modal-overlay-cart');
        close.classList.remove('active');
        modalContentCart.classList.remove('active');

    });



}

function renderCartItems(){

    const cartList =  document.getElementById('cart-items-list');
    if (!cartList) return;

    if (cart.length === 0){
        cartList.innerHTML = `<p>Your cart is empty</p>`;
        return;
    }

    cartList.innerHTML = cart.map(item =>{
        const productDetails = allProducts.find(p => p.id == item.id);
        if (!productDetails) return '';
        const subtotal = productDetails.price *item.quantity;
        return `
        <div class='cart-item'>
        <p>${productDetails.name} (Qnt: ${item.quantity})</p>
        <P>R$ ${subtotal.toFixed(2)}</P>
        
        </div>
        `;
    }).join('');
    }




//     // A MÁGICA COMEÇA AQUI
//     const cartHtmlItems = cart.map(item => {
//         // 1. Para cada item no carrinho, encontre os detalhes completos na nossa "enciclopédia"
//         const productDetails = allProducts.find(p => p.id == item.id);
//
//         // Se, por algum motivo, o produto não for encontrado, pulamos este item.
//         if (!productDetails) return '';
//
//         // 2. Calcule o subtotal para este item
//         const subtotal = productDetails.price * item.quantity;
//
//         // 3. Retorne a "planta" (string de HTML) para este item específico
//
//         // <img class="cart-item-img" src="${productDetails.imageUrl || '/assets/default-product.png'}" alt="${productDetails.name}">
//         return `
//             <div class="cart-item">
//
//                 <div class="cart-item-details">
//                     <span class="item-name">${productDetails.name}</span>
//                     <span class="item-price">Qtd: ${item.quantity} x R$ ${productDetails.price.toFixed(2)}</span>
//                 </div>
//                 <span class="item-subtotal">R$ ${subtotal.toFixed(2)}</span>
//
//             </div>
//         `;
//     }).join(''); // 4. Junte todas as "plantas" em uma única string gigante
//
//     // Agora sim, injetamos o HTML completo no container
//     cartContent.innerHTML = cartHtmlItems;
//
// }