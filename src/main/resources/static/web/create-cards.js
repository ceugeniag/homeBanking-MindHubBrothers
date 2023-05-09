const {createApp} = Vue

const app = createApp ({
    data(){
        return{
            clients:[],
            password:"",
            email:"",
            firstname:"",
            lastname:"",
            cardType:"",
            colorType:"",
            valorID :(new URLSearchParams(location.search)).get("id")
        }
    },
    created(){
        this.loadData()
    },
    methods:{
        loadData(){
            axios.get('http://localhost:8080/api/clients')
            .then(response => {
                this.clients = response.data;
                console.log(this.clients);
            })
            .catch(error=>{
                console.log(error);
            });
        },
        createCard(){
            Swal.fire({
                icon: 'warning',
                title: 'You are about to get a new card, are you sure?',
                showCancelButton: true,
                confirmButtonText: 'Yes, create it!',
                cancelButtonText: 'Cancel',
                preConfirm: () => {
                    return axios.post('/api/clients/current/cards',`cardType=${this.cardType}&colorType=${this.colorType}`)
                    .then ( response => 
                            Swal.fire({
                                    icon: 'success',
                                    title: 'You have a new Card!',
                                    showCancelButton: true,
                                    confirmButtonText: 'Accepted',
                                    cancelButtonText: 'Cancel',
                                    timer: 6000,
                                }) .then(()=> window.location.href = '/web/cards.html')
                            
                ).catch(error=>
                    Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: error.response.data,
                    timer: 6000,
                }))}
            })
            // .then((result) =>{
            //     if(result.isConfirmed){
            //         axios.post('/api/clients/current/accounts')
            //         .then(response =>{
            //             if (response.status == "201"){
            //                 axios.post('/api/clients/current/cards',`cardType=${this.cardType}&colorType=${this.colorType}`)
            //                 this.createCard = true,
            //                 this.loadData()
            //                 Swal.fire({
            //                         icon: 'success',
            //                         title: 'You have a new Card!',
            //                         showCancelButton: true,
            //                         confirmButtonText: 'Accepted',
            //                         cancelButtonText: 'Cancel',
            //                         timer: 6000,
            //                     })
            //                  (window.location.href = '/web/cards.html')
            //             }
            //         })
                
        }
        
    },
        logout(){
            axios.post('/api/logout')
            .then(response=> console.log(response), (window.location.href = '/web/index.html'))
            .catch(error => console.log(error));
        }
    },
    
    )
app.mount("#app")

