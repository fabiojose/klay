
// Create new stream from payments topic
def payments = builder.stream 'payments'

// Print the record value
payments
	.peek { k,v -> println v}
