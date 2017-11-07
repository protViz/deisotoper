package ch.fgcz.proteomics;

/**
 * @author Lucas Schmidt
 * @since 2017-11-07
 */

public class Version {
    private static String versionOfDTO() {
        return "Package: DTO, Date: 2017-11-07, Author: Lucas Schmidt";
    }

    private static String versionOfFBDM() {
        return "Package: FBDM, Date: 2017-11-07, Author: Lucas Schmidt";
    }

    private static String versionOfMSPY() {
        return "Package: MSPY, Date: 2017-11-07, Author: Lucas Schmidt";
    }

    private static String versionOfMGF() {
        return "Package: MGF, sDate: 2017-11-07, Author: Lucas Schmidt";
    }

    private static String versionOfRAdapter() {
        return "Package: RAdapter, Date: 2017-11-07, Author: Lucas Schmidt";
    }

    private static String versionOfUtilities() {
        return "Package: Utilities, Date: 2017-11-07, Author: Lucas Schmidt";
    }

    public static String version() {
        String linesep = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();

        sb.append(versionOfDTO()).append(linesep);
        sb.append(versionOfFBDM()).append(linesep);
        sb.append(versionOfMSPY()).append(linesep);
        sb.append(versionOfMGF()).append(linesep);
        sb.append(versionOfRAdapter()).append(linesep);
        sb.append(versionOfUtilities()).append(linesep);

        return sb.toString();
    }
}