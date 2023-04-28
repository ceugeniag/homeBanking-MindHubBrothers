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
            axios.post('/api/clients/current/cards',`cardType=${this.cardType}&colorType=${this.colorType}`,{
                headers:{'content-type':'application/x-www-form-urlencoded'}})
                .then(response => console.log(response),console.log(this.clients), (window.location.href = '/web/cards.html'))
        },
        logout(){
            axios.post('/api/logout')
            .then(response=> console.log(response), (window.location.href = '/web/index.html'))
            .catch(error => console.log(error));
        }
    },
    
    })
app.mount("#app")

