const {createApp} = Vue

const app = createApp ({
    data(){
        return{
            clients:[],
            accounts:[],
            loans:[],
            valorID :(new URLSearchParams(location.search)).get("id"),
            id:"",
            SAVINGS:"",
            CHECKING:"",
            accountType:"",
            idLoan:0,
            account:"",
            amount:0,
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
                this.accounts = this.clients.accounts.filter(account => account.active)
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
                    axios.post(`/api/clients/current/accounts?accountType=${this.accountType}`)
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

                    // deleteAccounts(id){
                    // axios.put(`/api/clients/current/accounts?id=${id}`)
                    //     .then(response => console.log("delete"))
                    //     .catch(error => console.log(error))
                    // },

                        deleteAccounts(id) {
                            Swal.fire({
                            icon: 'warning',
                            title: 'You are about to delete an account, are you sure?',
                            showCancelButton: true,
                            confirmButtonText: 'Delete it!',
                            cancelButtonText: 'Cancel',
                            timer: 6000,
                            }).then((result) => {
                            if (result.isConfirmed) {
                                axios.put(`/api/clients/current/accounts?id=${id}`)
                                .then(response => {
                                    if (response.status === 200) {
                                    this.deleteAccounts = true;
                                    Swal.fire({
                                        icon: 'success',
                                        title: 'You just deleted your account',
                                        showCancelButton: true,
                                        confirmButtonText: 'Accepted',
                                        cancelButtonText: 'Cancel',
                                        timer: 6000,
                                    }).then(() => {
                                        window.location.reload(); // Recargar la página
                                    });
                                    }
                                })
                                .catch(error => {
                                    Swal.fire({
                                    icon: 'error',
                                    title: 'Error',
                                    text: error.response.data,
                                    timer: 6000,
                                    });
                                });
                            }
                            });
                        },
                        pay() {
                            console.log(this.idLoan);
                            console.log(this.account);
                            console.log(this.amount);
                            Swal.fire({
                                title: 'Pay loan',
                                text: 'You are about to make a payment on your loan, are you sure?',
                                icon: 'warning',
                                showCancelButton: true,
                                confirmButtonColor: '#3085d6',
                                cancelButtonColor: '#d33',
                                confirmButtonText: 'Yes',
                                cancelButtonText: 'Cancel'
                            }).then((result) => {
                                if (result.isConfirmed) {
                                    axios.post(`/api/current/loans?idLoan=${this.idLoan}&account=${this.account}&amount=${this.amount}` )
                                .then(response =>{
                                    if (response.status == "201"){
                                        this.pay = true,
                                    
                                Swal.fire({
                                                    icon: 'success',
                                                    title: response.data,
                                                    showCancelButton: true,
                                                    confirmButtonText: 'Accepted',
                                                    cancelButtonText: 'Cancel',
                                                    timer: 6000,
                                                })
                                    (window.location.href = '/web/accounts.html')
                                            }
                                })
                                .catch(error => console.log(error), Swal.fire({
                                    icon: 'error',
                                    text: error.response.data,
                                }))
                            }
                        })
                    },
                
    }})
app.mount("#app")
