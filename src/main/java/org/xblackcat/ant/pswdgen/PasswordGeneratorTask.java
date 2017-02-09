package org.xblackcat.ant.pswdgen;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 05.02.2015 10:17
 *
 * @author xBlackCat
 */
public class PasswordGeneratorTask extends Task {
    private static final String ALPHA = "abcdefghijklmopqastuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final String NON_LETTERS = "!@#%^&*()_+-}{][~|?.";

    private boolean alpha = true;
    private boolean numeric = true;
    private boolean nonLetters = false;
    private boolean lowercase = false;
    private int length = 32;
    private String name;
    private boolean useSecureRnd = false;

    public void setAlpha(boolean alpha) {
        this.alpha = alpha;
    }

    public void setNumeric(boolean numeric) {
        this.numeric = numeric;
    }

    public void setNonLetters(boolean nonLetters) {
        this.nonLetters = nonLetters;
    }

    public void setLowercase(boolean lowercase) {
        this.lowercase = lowercase;
    }

    public void setLength(int length) {
        if (length <= 0) {
            throw new BuildException("Password length should be positive number", getLocation());
        }
        this.length = length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUseSecureRnd(boolean useSecureRnd) {
        this.useSecureRnd = useSecureRnd;
    }

    @Override
    public void execute() throws BuildException {
        if (getProject() == null) {
            throw new IllegalStateException("project has not been set");
        }

        if (name == null) {
            throw new BuildException("You must specify property name for storing generated value", getLocation());
        }

        final Random rnd;
        if (useSecureRnd) {
            rnd = new SecureRandom();
        } else {
            rnd = new Random();
        }

        StringBuilder origin = new StringBuilder();
        if (alpha) {
            origin.append(ALPHA);
            if (nonLetters) {
                origin.append(ALPHA);
            }
        }

        if (numeric) {
            origin.append(NUMERIC);
            if (nonLetters) {
                origin.append(NUMERIC);
            }
        }

        if (nonLetters) {
            origin.append(NON_LETTERS);
        }

        StringBuilder pswd = new StringBuilder();
        while (pswd.length() < length) {
            int idx = rnd.nextInt(origin.length());
            char c = origin.charAt(idx);
            if (!lowercase && Character.isLetter(c) && rnd.nextBoolean()) {
                c = Character.toUpperCase(c);
            }

            pswd.append(c);
        }

        PropertyHelper ph = PropertyHelper.getPropertyHelper(getProject());
        ph.setNewProperty(name, pswd.toString());

    }

}
