package ch.fgcz.proteomics;

/**
 * @author Lucas Schmidt
 * @since 2017-11-07
 */

public class Version {
    private static final String DTO = "Java Package: DTO, Date: 2017-12-20, Author: Lucas Schmidt";
    private static final String FBDM = "Java Package: FBDM, Date: 2017-12-20, Author: Lucas Schmidt";
    private static final String MSPY = "Java Package: MSPY (Deprecated), Date: 2017-12-14, Author: Lucas Schmidt";
    private static final String MGF = "Java Package: MGF, Date: 2017-12-20, Author: Lucas Schmidt";
    private static final String R = "Java Package: RAdapter, Date: 2017-12-20, Author: Lucas Schmidt";
    private static final String UTILITIES = "Java Package: Utilities, Date: 2017-11-22, Author: Lucas Schmidt";
    private static final String JAVA = "Java Version: " + System.getProperty("java.version");

    public static String version() {
        String linesep = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();

        sb.append(DTO).append(linesep);
        sb.append(FBDM).append(linesep);
        sb.append(MSPY).append(linesep);
        sb.append(MGF).append(linesep);
        sb.append(R).append(linesep);
        sb.append(UTILITIES).append(linesep);
        sb.append(JAVA);

        return sb.toString();
    }

    private Version() {
        // Empty constructor
    }
}
