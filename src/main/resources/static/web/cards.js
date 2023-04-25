const {createApp} = Vue

const app = createApp ({
    data(){
        return{
            clients:[],
            cards:[],
            credit:[],
            debit:[],
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
                this.cards= this.clients.cards;
                console.log(this.cards);
                this.credit= this.cards.filter(card => card.type === "CREDIT");
                console.log(this.credit);
                this.debit=this.cards.filter(card => card.type === "DEBIT");
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
        }
    },
    
    })
app.mount("#app")