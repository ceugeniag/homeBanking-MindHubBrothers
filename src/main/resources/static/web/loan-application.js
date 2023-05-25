const {createApp} = Vue

const app = createApp ({
    data(){
        return{
            client:[],
            clients:[],
            accounts:[],
            loans:[],
            numberAccountDestiny:"",
            loanId:0,
            amount:0,
            payment:'',
            totalLoanPrice: 0,
            payments:0,
            selectedLoan:0,
            filteredPayments:[],
            selectedPayment:[],
        }
    },
    created(){
        this.loadData()
        this.loansData()
        this.updateTotalPrice();

    },
    methods:{
        loadData(){
            axios.get('/api/clients/current')
            .then(response => {
                this.client = response.data;
                console.log(this.client);
                this.accounts = this.client.accounts.filter(account => account.active)
                console.log(this.accounts);
            })
            .catch(error=>{
                console.log(error);
            });
        },
        logout(){
            axios.post('/api/logout')
            .then(response=> console.log('signed out!!!'), (window.location.href = '/web/index.html'))
            .catch(error => console.log(error))
        },
        loansData(){
            axios.get('/api/loans')
            .then(response => {
                console.log("A ver si entró")
                this.loans = response.data
                console.log(this.loans)
                })
            .catch(error=> console.log(error))
        },
        // loanApply(){
        //     console.log(this.selectedLoan)
        //     console.log(this.amount)
        //     console.log(this.selectedPayment)
        //     console.log(this.numberAccountDestiny)
        //     axios.post('/api/loans', {
        //         loanId: this.selectedLoan,
        //         amount: this.amount,
        //         payments: this.selectedPayment,
        //         numberAccountDestiny: this.numberAccountDestiny,
        //     })
        //     .then(response => {
        //         console.log(response.data)
        //     })
        //     .catch(error => Swal.fire({
        //         icon: 'error',
        //         text: error.response.data,
        //             }))
        // },

        loanApply() {
            Swal.fire({
              title: ' Apply loan',
              text: 'You are about to apply for a new loan, are you sure?',
              icon: 'warning',
              showCancelButton: true,
              confirmButtonColor: '#3085d6',
              cancelButtonColor: '#d33',
              confirmButtonText: 'Apply',
              cancelButtonText: 'Cancel'
            }).then((result) => {
              if (result.isConfirmed) {
                axios.post('/api/loans', {
                  loanId: this.selectedLoan,
                  amount: this.amount,
                  payments: this.selectedPayment,
                  numberAccountDestiny: this.numberAccountDestiny,
                })
                .then(response =>{
                    if (response.status == "201"){
                        this.loanApply = true,
                        this.loadData()
                    
                Swal.fire({
                                    icon: 'success',
                                    title: 'You have a new Loan!',
                                    showCancelButton: true,
                                    confirmButtonText: 'Accepted',
                                    cancelButtonText: 'Cancel',
                                    timer: 6000,
                                })
                      (window.location.href = '/web/accounts.html')

                            }
                })
                .catch(error => Swal.fire({
                  icon: 'error',
                  text: error.response.data,
                }))
              }
            })
          },
          
        calculateLoanTotal(amount) {
            const total = amount * (1.2);
            return total.toFixed(2);
        },
        updateTotalPrice() {
            const amount = this.amount;
            const total = this.calculateLoanTotal(amount);
            this.totalLoanPrice = total;
          },
          selectedLoan() {
            this.selectedLoan = this.loanId;
            console.log(this.selectedLoan)
            return this.loans.find(loan => loan.id === selectedLoan);
          },
          loadPayments() {
            if (this.selectedLoan) {
              const selectedLoan = this.loans.find(loan => loan.id === this.selectedLoan)
              console.log(selectedLoan);
              if (selectedLoan.payments && selectedLoan.payments.length > 0) {
                this.filteredPayments = selectedLoan.payments
              } else {
                this.filteredPayments = []
              }
            } else {
              this.filteredPayments = []
            }
            this.selectedPayment = null
          },
          getMaxAmount(loanId) {
            const loan = this.loans.find(loan => loan.id === loanId)
            return loan ? loan.maxAmount : 0
          },
    },
    })
app.mount("#app")