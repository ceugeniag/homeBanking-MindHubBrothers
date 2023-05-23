const {createApp} = Vue

const app = createApp ({
    data(){
        return{
            clients:[],
            password:"",
            email:"",
            firstname:"",
            lastname:"",
            valorID :(new URLSearchParams(location.search)).get("id")
        }
    },
    created(){
        this.loadData()
    },
    methods:{
        loadData(){
            axios.get('/api/clients')
            .then(response => {
                this.clients = response.data;
                console.log(this.clients);
            })
            .catch(error=>{
                console.log(error);
            });
        },
        logIn(){
            axios
            .post('/api/login', 'email=' + this.email + '&password=' + this.password)
            .then(()=> (window.location.href = '/web/accounts.html'))
            .catch(error => {
                Swal.fire({
                    icon: 'error',
                    text: 'Something went wrong, your email or password is incorrect',
                    footer: 'Are you sure you are already registered?'
                        })
                    })
            },
        singUp() {
            axios
                .post('/api/clients', `firstName=${this.firstname}&lastName=${this.lastname}&email=${this.email}&password=${this.password}`, {
                    headers: { 'content-type': 'application/x-www-form-urlencoded' }
                })
                .then(response => console.log(response), )
                .catch(error => {
                    Swal.fire({
                        icon: 'error',
                        text: error.response.data,
                    })
                    })
            },
        logout(){
            axios
            .post('/api/logout')
            .then(response=> (window.location.href = '/web/index.html'), 
            console.log('signed out!!!'),)
            .catch(error => {
                Swal.fire({
                    icon: 'error',
                    text: error.response.data,
                    })
                })
            },
    },
    })
app.mount("#app")


// Swal.fire({
//     icon: 'error',
//     title: 'Oops...',
//     text: 'Something went wrong!',
//     footer: '<a href="">Why do I have this issue?</a>'
// })

// newAccount() {
//     Swal.fire({
//         icon: 'warning',
//         title: 'You are creating a new Account..¿Are you sure?',
//         showCancelButton: true,
//         confirmButtonText: 'Yes, create new Account',
//         cancelButtonText: 'Cancel',
//         timer: 6000,
//     }).then((result) => {
//         if (result.isConfirmed) {
//             axios.post('/api/clients/current/accounts')
//                 .then(response => {
//                     if (response.status == "201") {
//                             this.createdAccount = true,
//                             this.loadData()
//                             Swal.fire({
//                                 icon: 'success',
//                                 title: 'You have a new Account!',
//                                 showCancelButton: true,
//                                 confirmButtonText: 'Accepted',
//                                 cancelButtonText: 'Cancel',
//                                 timer: 6000,
//                             })
//                     }
//                 })
//                 .catch(error => Swal.fire({
//                     icon: 'error',
//                     title: 'Error',
//                     text: error.response.data,
//                     timer: 6000,
//                 }))
//         }
//     })
// },