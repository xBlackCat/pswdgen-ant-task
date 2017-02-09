package org.xblackcat.ant.pswdgen;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;
import org.eclipse.jetty.util.security.UnixCrypt;

/**
 * 05.02.2015 15:16
 *
 * @author xBlackCat
 */
public class UnixCryptTask extends Task {
    private String passwd;
    private String user;
    private String name;

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void execute() throws BuildException {

        if (name == null) {
            throw new BuildException("You must specify property name for storing generated value", getLocation());
        }

        if (user == null) {
            throw new BuildException("You must specify user name for building encrypted password", getLocation());
        }

        if (passwd == null) {
            throw new BuildException("You must specify password for building encrypted password", getLocation());
        }

        String crypted = UnixCrypt.crypt(passwd, user);

        PropertyHelper ph = PropertyHelper.getPropertyHelper(getProject());
        ph.setNewProperty(name, crypted);
    }
}
