package ch.fgcz.proteomics;

/**
 * @author Lucas Schmidt
 * @since 2017-11-07
 */

public class Version {
    private static String versionOfDTO() {
        return "Java Package: DTO, Date: 2017-11-07, Author: Lucas Schmidt";
    }

    private static String versionOfFBDM() {
        return "Java Package: FBDM, Date: 2017-11-15, Author: Lucas Schmidt";
    }

    private static String versionOfMSPY() {
        return "Java Package: MSPY, Date: 2017-11-07, Author: Lucas Schmidt";
    }

    private static String versionOfMGF() {
        return "Java Package: MGF, sDate: 2017-11-07, Author: Lucas Schmidt";
    }

    private static String versionOfRAdapter() {
        return "Java Package: RAdapter, Date: 2017-11-15, Author: Lucas Schmidt";
    }

    private static String versionOfUtilities() {
        return "Java Package: Utilities, Date: 2017-11-07, Author: Lucas Schmidt";
    }

    public static String version() {
        String linesep = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();

        sb.append(versionOfDTO()).append(linesep);
        sb.append(versionOfFBDM()).append(linesep);
        sb.append(versionOfMSPY()).append(linesep);
        sb.append(versionOfMGF()).append(linesep);
        sb.append(versionOfRAdapter()).append(linesep);
        sb.append(versionOfUtilities());

        return sb.toString();
    }
}
