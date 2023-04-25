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
            axios.get('http://localhost:8080/api/clients')
            .then(response => {
                this.clients = response.data;
                console.log(this.clients);
            })
            .catch(error=>{
                console.log(error);
            });
        },
        logIn(){
            console.log(this.email)
            axios
            .post('/api/login', 'email=' + this.email + '&password=' + this.password)
            .then(response=> (window.location.href = '/web/accounts.html'))
            .catch(error => console.log(error));
        },
        // singUp(){
        //     axios
        //     .post('api/clients', 'firstName=' + this.firstname + '&lastName=' + this.lastname + '&email=' + this.email + '&password=' + this.password,{
        //     headers:{'content-type':'application/x-www-form-urlencoded'}})
        //     .then(response => (this.logIn))
        //     .catch(error => console.log(error));
        // },
        singUp() {
            axios
                .post('/api/clients', `firstName=${this.firstname}&lastName=${this.lastname}&email=${this.email}&password=${this.password}`, {
                    headers: { 'content-type': 'application/x-www-form-urlencoded' }
                })
                .then(response => console.log(response))
        },
        logout(){
            axios.post('/api/logout')
            .then(response=> console.log('signed out!!!'), (window.location.href = '/web/index.html'))
            .catch(error => console.log(error));
        }
    },
    
    })
app.mount("#app")

// axios.post('/api/login',"email=melba@mindhub.com&password=melba",{headers:{'content-type':'application/x-www-form-urlencoded'}}).then(response => console.log('signed in!!!'))
//axios.post('/api/clients',"firstName=pedro2&lastName=rodriguez&email=pedro@mindhub.com&password=pedro",{headers:{'content-type':'application/x-www-form-urlencoded'}}).then(response => console.log('registered'))
//axios.post('/api/logout').then(response => console.log('signed out!!!'))

