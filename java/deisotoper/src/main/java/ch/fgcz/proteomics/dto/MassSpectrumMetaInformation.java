package ch.fgcz.proteomics.dto;

import java.util.List;

public interface MassSpectrumMetaInformation {

    double getPeptideMass();

    int getChargeState();

    List<Double> getMz();
    List<Double> getIntensity();
}
