const {createApp} = Vue

const app = createApp ({
    data(){
        return{
            clients:[],
            cards:[],
            credit:[],
            debit:[],
            valorID :(new URLSearchParams(location.search)).get("id"),
            number:"",
            active:[],
            // actualDate: new Date().toLocaleDateString().split(",")[0].split("/").reverse().join("-")
        }
    },
    created(){
        this.loadData()
    },
    methods:{
        loadData(){
            axios.get('/api/clients/current')
            .then(response => {
                this.clients = response.data;
                console.log(this.clients);
                this.cards= this.clients.cards;
                console.log(this.cards);
                this.credit= this.cards.filter(card => card.type === "CREDIT" && card.active);
                console.log(this.credit);
                this.debit=this.cards.filter(card => card.type === "DEBIT" && card.active);
                console.log(this.debit);
            })
            .catch(error=>{
                console.log(error);
            });
        },
        logout(){
            axios.post('/api/logout')
            .then(response=> console.log('signed out!!!'), (window.location.href = '/web/index.html'))
            .catch(error => console.log(error));
        },

        // deleteCards(number){
        //     axios.put(`/api/clients/current/cards?number=${number}`)
        //     .then(response => console.log("delete"))
        //     .catch(error => console.log(error))
        // },
        deleteCards(number){
            Swal.fire({
                icon: 'warning',
                title: 'You are about to delete a card, are you sure?',
                showCancelButton: true,
                confirmButtonText: 'Delete it!',
                cancelButtonText: 'Cancel',
                timer: 6000,
            })
            .then((result)=> {
                if(result.isConfirmed){
                    axios.put(`/api/clients/current/cards?number=${number}`)
                    .then(response =>{
                        if (response.status == "200"){
                            this.deleteAccounts = true,
                            this.loadData()
                            Swal.fire({
                                icon: 'success',
                                title: 'You just deleted your card',
                                showCancelButton: true,
                                confirmButtonText: 'Accepted',
                                cancelButtonText: 'Cancel',
                                timer: 6000,
                            })
                            .then(() => {
                                location.reload(); 
                            })
                            .catch(error =>
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Error',
                                    text: error.response.data,
                                    timer: 6000,
                                }))                                    
                            }
                    } )
                }
            })
        },
        isCardExpiring(card) {
            const expirationDate = new Date(card.thruDate);
            const today = new Date();
            const oneMonthFromNow = new Date();
            oneMonthFromNow.setMonth(today.getMonth() + 1);
            if (expirationDate < today) {
                return "Expired";
            } else if (expirationDate < oneMonthFromNow) {
                return "This card is about to expire";
            } else {
                return "Valid";
            }
        },

        
    },
    
    })
app.mount("#app")