import java.time.Duration

// Simple filter, that not will used in the main stream
stream.filter { k,v -> k.equals '2002' }
	.peek { k,v -> println v.id}

// Create new strem from payments topic
def payments = builder.stream 'payments'

// Joint orders and payments within window of five minutes
stream.join(payments,
	{ order, payment -> [ orderId: order.id, payCode: payment.code ]},
	JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(5)))
	.peek { k,v -> println v}
