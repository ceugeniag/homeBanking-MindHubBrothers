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
            axios.post('/api/logout')
            .then(response=> console.log('signed out!!!'), (window.location.href = '/web/index.html'))
            .catch(error => console.log(error));
        }
    },
    
    })
app.mount("#app")