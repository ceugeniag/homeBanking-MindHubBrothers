const {createApp} = Vue
const app = createApp ({
    data(){
        return{
            clients:[],
            account:[],
            transactions:[],
            valorID :(new URLSearchParams(location.search)).get("id"),
        }
    },
    created(){
        this.loadData()
        this.sorted(this.transactions)
    },
    methods:{
        loadData(){
            axios.get('http://localhost:8080/api/accounts/'+ this.valorID)
            .then(response => {
                this.account = response.data;
                console.log(this.account);
                this.transactions= this.account.transactions;
                console.log(this.transactions);
            })
            .catch(error=>{
                console.log(error);
            });
        },
        sorted(){
            if (this.transactions != "") {
                return this.transactions.sort((a,b) => b.transactionsDate - a.transactionsDate)}         
        }
    },
    })
app.mount("#app")