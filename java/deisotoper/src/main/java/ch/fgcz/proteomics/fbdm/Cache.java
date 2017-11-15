package ch.fgcz.proteomics.fbdm;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.GraphPath;

import ch.fgcz.proteomics.dto.MassSpectrum;

/**
 * @author Lucas Schmidt
 * @since 2017-11-15
 */

public class Cache {
    private MassSpectrum massspectrum;
    private MassSpectrum massspectrumdeisotoped;
    private IsotopicMassSpectrum isotopicmassspectrum;
    private List<IsotopicClusterGraph> isotopicclustergraphs = new ArrayList<>();
    private List<GraphPath<IsotopicCluster, Connection>> bestpaths = new ArrayList<>();

    public MassSpectrum getMassSpectrum() {
        return massspectrum;
    }

    public void setMassSpectrum(MassSpectrum massspectrum) {
        this.massspectrum = massspectrum;
    }

    public MassSpectrum getMassSpectrumDeisotoped() {
        return massspectrumdeisotoped;
    }

    public void setMassSpectrumDeisotoped(MassSpectrum massspectrumdeisotoped) {
        this.massspectrumdeisotoped = massspectrumdeisotoped;
    }

    public IsotopicMassSpectrum getIsotopicMassSpectrum() {
        return isotopicmassspectrum;
    }

    public void setIsotopicMassSpectrum(IsotopicMassSpectrum isotopicmassspectrum) {
        this.isotopicmassspectrum = isotopicmassspectrum;
    }

    public List<IsotopicClusterGraph> getIsotopicClusterGraphs() {
        return isotopicclustergraphs;
    }

    public void setIsotopicClusterGraphs(List<IsotopicClusterGraph> isotopicclustergraphs) {
        this.isotopicclustergraphs = isotopicclustergraphs;
    }

    public List<GraphPath<IsotopicCluster, Connection>> getBestPaths() {
        return bestpaths;
    }

    public void setBestPaths(List<GraphPath<IsotopicCluster, Connection>> bestpaths) {
        this.bestpaths = bestpaths;
    }

    public void addIsotopicClusterGraph(IsotopicClusterGraph isotopicclustergraph) {
        this.isotopicclustergraphs.add(isotopicclustergraph);
    }

    public void addBestPath(GraphPath<IsotopicCluster, Connection> bestpath) {
        this.bestpaths.add(bestpath);
    }
}
