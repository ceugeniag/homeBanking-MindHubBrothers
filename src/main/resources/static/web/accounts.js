const {createApp} = Vue

const app = createApp ({
    data(){
        return{
            clients:[],
            firstName: "",
            lastName:"",
            email:"",
            balance:"",
            creationDate:"",
            number:"",
            accounts:"",
            valorID :(new URLSearchParams(location.search)).get("id")
        }
    },
    created(){
        this.loadData()
    },
    methods:{
        loadData(){
            axios.get('http://localhost:8080/api/clients/1')
            .then(response => {
                this.clients = response.data;
                console.log(this.clients);
                this.accounts = this.clients.accounts
                console.log(this.accounts);

            })
            .catch(error=>{
                console.log(error);
            });
        },
    },
    
    })
app.mount("#app")