const {createApp} = Vue

const app = createApp ({
    data(){
        return{
            clients:[],
            accounts:[],
            loans:[],
            valorID :(new URLSearchParams(location.search)).get("id")
        }
    },
    created(){
        this.loadData()
    },
    methods:{
        loadData(){
            axios.get('http://localhost:8080/api/clients/current')
            .then(response => {
                this.clients = response.data;
                console.log(this.clients);
                this.accounts = this.clients.accounts;
                console.log(this.accounts);
                this.loans=this.clients.loans;
                console.log(this.loans);

            })
            .catch(error=>{
                console.log(error);
            });
        },
        logout(){
            axios.post('/api/logout'),{
            headers: { 'content-type': 'application/x-www-form-urlencoded'}}
            .then(response=> console.log('signed out!!!'), (window.location.href = '/web/index.html'))
            .catch(error => console.log(error))
        },
        createdAccount(){
            Swal.fire({
                icon: 'warning',
                title: 'You are about to create a new account, are you sure?',
                showCancelButton: true,
                confirmButtonText: 'Yes, create it!',
                cancelButtonText: 'Cancel',
                timer: 6000,
            })
            .then((result) =>{
                if(result.isConfirmed){
                    axios.post('/api/clients/current/accounts')
                    .then(response =>{
                        if (response.status == "201"){
                            this.createdAccount = true,
                            this.loadData()
                            Swal.fire({
                                    icon: 'success',
                                    title: 'You have a new Account!',
                                    showCancelButton: true,
                                    confirmButtonText: 'Accepted',
                                    cancelButtonText: 'Cancel',
                                    timer: 6000,
                                })
                        }
                    })
                    .catch(error => Swal.fire({
                                    icon: 'error',
                                    title: 'Error',
                                    text: error.response.data,
                                    timer: 6000,
                                }))
                            }
                        })
                    },
    
    },
    
    })
app.mount("#app")
