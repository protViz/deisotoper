package ch.fgcz.proteomics;

/**
 * @author Lucas Schmidt
 * @since 2017-11-07
 */

public class Version {
    private static String versionOfDto() {
        return "Java Package: DTO, Date: 2017-12-04, Author: Lucas Schmidt";
    }

    private static String versionOfFbdm() {
        return "Java Package: FBDM, Date: 2017-12-08, Author: Lucas Schmidt";
    }

    private static String versionOfMspy() {
        return "Java Package: MSPY (Deprecated), Date: 2017-12-04, Author: Lucas Schmidt";
    }

    private static String versionOfMgf() {
        return "Java Package: MGF, sDate: 2017-12-04, Author: Lucas Schmidt";
    }

    private static String versionOfRAdapter() {
        return "Java Package: RAdapter, Date: 2017-12-04, Author: Lucas Schmidt";
    }

    private static String versionOfUtilities() {
        return "Java Package: Utilities, Date: 2017-11-22, Author: Lucas Schmidt";
    }

    private static String javaVersion() {
        return "Java Version: " + System.getProperty("java.version");
    }

    public static String version() {
        String linesep = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();

        sb.append(versionOfDto()).append(linesep);
        sb.append(versionOfFbdm()).append(linesep);
        sb.append(versionOfMspy()).append(linesep);
        sb.append(versionOfMgf()).append(linesep);
        sb.append(versionOfRAdapter()).append(linesep);
        sb.append(versionOfUtilities()).append(linesep);
        sb.append(javaVersion());

        return sb.toString();
    }
}
