document.addEventListener('DOMContentLoaded', () => {

    const container = document.getElementById("product-container");
    const menuToggles = document.querySelectorAll('.menu-toggle');
    const getAllProductsBtn = document.getElementById('btn-list-products');


    let productVisible = false;

    menuToggles.forEach(toggle => {

        toggle.addEventListener('click', () => {

            const menuItem = toggle.parentElement;

            menuItem.classList.toggle('active');

        });
    });

    getAllProductsBtn.addEventListener('click', (event) => {
        event.preventDefault();
        toggleProductsVisibility();
    });

    function toggleProductsVisibility() {
        if (productVisible === false) {
            loadProducts();
            productVisible = true;
        } else {
            container.innerHTML = "";
            productVisible = false;
        }
    }

    async function loadProducts() {
        try {
            const answer = await fetch("http://localhost:8080/api/v1/products");
            const products = await answer.json();

            container.innerHTML = "";

            products.forEach(product => {
                const card = document.createElement('div');
                card.classList.add("card");

                card.innerHTML = `
                    <h3>${product.name}</h3>
                    <p>${product.description}</p>
                    <strong>R$ ${product.price.toFixed(2)}</strong>
                `;

                container.appendChild(card);
            });
        } catch (error) {
            console.error("Error in load products: ", error);
        }
    }
});