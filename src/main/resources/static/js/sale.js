addEventListener('DOMContentLoaded', () => {

    const finishBuyByn = document.getElementById('btn-checkout');
    const finishBuyBynDirect = document.getElementById('direct-payment-btn');


    if (finishBuyByn){
        finishBuyByn.addEventListener('click', createSale);
    }

    if (finishBuyBynDirect){
        finishBuyBynDirect.addEventListener('click', createSale);
    }
})

async function createSale(){

    console.log('starting sale process.')

    if (cart.length === 0){
        console.log("The cart is empty.")
        alert('Your cart is empty.')
    }

    const userDataStr = localStorage.getItem('userData'); //take the role of user
    const token = localStorage.getItem('authToken'); //and take your token

    if (!userDataStr || !token){
        alert('You need stay logged for finish the buy.');
        console.log('The user not is logged.');
        return;
    }

    const productItems = cart.map(item => {
        return {product_id: item.id, quantity: item.quantity}
    });

    const userData = JSON.parse(userDataStr);

    //NOW CREATE THE JSON FOR BACK END

    const createSaleDTO = {

        customerId: "lucas.customer",
        employeeId: userData.email,
        petId: null,
        productsItems: productItems,
        serviceItems: [],
        paymentMode: "MONEY"

    };

    try{

        const response = await fetch('http://localhost:8080/api/v1/sales',{

            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(createSaleDTO)

        });

        if (response.ok){
            const saleResponse = await response.json();
            console.log("Request Successfully, we´ll direct you to payment page.");
            window.location.href = saleResponse.paymentUrl;

        }else{
            const errorData = await response.json();
            alert(`Error in create sale: ${errorData.message}`);
        }
    }catch (error){
        console.log('Error in sale POST request', error)
    }
}