package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cusData.ActionType;
import cusData.CusAction;
import cusData.CusResult;
import cusServices.CusService;

public class CusQueryThread extends Thread {

	CustomerServer cServer;
	CusService cusService;

	Socket connection;
	ObjectOutputStream output;
	ObjectInputStream input;

	public CusQueryThread(Socket socket, CusService cusService,
			CustomerServer cs) {
		this.connection = socket;
		this.cusService = cusService;
		this.cServer = cs;
	}

	@Override
	public void run() {
		try {
			cServer.appendContent("\n------------------------------");
			cServer.appendContent("Connection - From: "
					+ connection.getInetAddress());

			input = new ObjectInputStream(connection.getInputStream());
			output = new ObjectOutputStream(connection.getOutputStream());

			Object obj = input.readObject();
			CusResult result = null;

			if (obj != null) {
				if (obj instanceof CusAction) {
					CusAction action = (CusAction) obj;
					cServer.appendContent("Action: " + action);

					result = doTask(action);	// do update operations
				}
				output.writeObject(result); // return result to client
			}
			connection.close();
			output.close();
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public CusResult doTask(CusAction action) throws InterruptedException {
		CusResult result;
		String phone = action.getPhoneNum();
		float amount = action.getAmount();

		if (action.getAction() == ActionType.payment) { // payment
			result = cusService.doPayment(phone, amount);
		} else {	// purchase
			result = cusService.doPurchase(phone, amount);
		}

		return result;
	}

}
