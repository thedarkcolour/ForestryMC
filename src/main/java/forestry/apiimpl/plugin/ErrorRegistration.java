package forestry.apiimpl.plugin;

import java.util.ArrayList;

import forestry.api.core.IError;
import forestry.api.plugin.IErrorRegistration;

public class ErrorRegistration implements IErrorRegistration {
	private final ArrayList<IError> errors = new ArrayList<>();

	@Override
	public void registerError(IError error) {
		this.errors.add(error);
	}

	public ArrayList<IError> getErrors() {
		return this.errors;
	}
}
