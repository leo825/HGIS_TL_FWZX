package cn.geobeans.common.model;

public class SysException extends RuntimeException {

	public SysException() {
		super();
	}

	public SysException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SysException(String arg0) {
		super(arg0);
	}

	public SysException(Throwable arg0) {
		super(arg0);
	}

}
