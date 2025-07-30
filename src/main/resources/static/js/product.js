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

    getAllProductsBtn.addEventListener('click', toggleProductsVisibility);

    function toggleProductsVisibility(){

        if(productVisible === false){
            renderAllProducts();
            productVisible = true;
        }else{
            container.innerHTML = '';
            productVisible = false;
        }
    }

    function renderAllProducts() {

      

        try {

            container.innerHTML = "";

            let altLegend = null;

            allProducts.forEach(product => {
                const card = document.createElement('div');
                card.classList.add("card");

                altLegend = (product.imageUrl != null) ? `Image of ${product.imageUrl}` : `${product.name} without image yet`;

                const imageHTML = (product.imageUrl != null) ? `

                    <div class="card-img-item">
                         <img src="${product.imageUrl ||'/assets/defaul-produts.png-example'}" alt="${altLegend}">
                    </div>` : "";

                card.innerHTML = `

                    ${imageHTML}
                    <h3>${product.name}</h3>
                    <p>${product.description}</p>
                    <strong>R$ ${product.price.toFixed(2)}</strong>


                   <div class="cart-actions">
                       <button class="btn-add-to-cart" data-product-id="${product.id}">
                        <i class="bi bi-cart-plus"></i> Add to cart </button>
                   </div>
                            `;

                container.appendChild(card);
            });


            const addItemToCart = document.querySelectorAll('.btn-add-to-cart');

                addItemToCart.forEach(button => {
                    button.addEventListener('click', (event) => {
                        const productId = event.target.getAttribute('data-product-id');
                        addProductToCart(productId, 1);
                    });
                });
        } catch (error) {
            console.error("Error in load products: ", error);
        }
    }

});