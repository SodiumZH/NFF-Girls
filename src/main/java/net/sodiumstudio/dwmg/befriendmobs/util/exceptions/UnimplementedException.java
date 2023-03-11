package net.sodiumstudio.dwmg.befriendmobs.util.exceptions;

// Exception thrown on calling unimplemented code.
public class UnimplementedException extends RuntimeException {

	public UnimplementedException(String s)
	{
		super(s);
	}
	
	public UnimplementedException()
	{
		this("Attempting to call unimplemented class/function");
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 4279326289914680096L;

}
