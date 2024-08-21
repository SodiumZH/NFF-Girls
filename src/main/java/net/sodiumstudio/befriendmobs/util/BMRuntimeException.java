package net.sodiumstudio.befriendmobs.util;

public class BMRuntimeException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6582044165992602149L;

	protected String msg;
	
	public BMRuntimeException(String msg)
	{
		super(msg);
		this.msg = msg;
	}
	
	@Override
	public String getMessage()
	{
		return msg;
	}
	
}
