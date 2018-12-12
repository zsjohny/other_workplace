package com.tunnel.dao.impl

import com.tunnel.dao.BaseDomainDao
import com.tunnel.domain.*
import com.tunnel.repository.*
import com.tunnel.util.BaseDomain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils

/**
 * Created by nessary on 16-10-9.
 */
@Repository
class BaseDomainImpl implements BaseDomainDao {
    @Autowired
    private UserRepository userRepository
    @Autowired
    private UserCrudRepository userCrudRepository

    @Autowired
    private ProjectCrudRepository projectCrudRepository
    @Autowired
    private ProjectRepository projectRepository

    @Autowired
    private TunnelInfoRepository tunnelInfoRepository
    @Autowired
    private TunnelInfoCrudRepository tunnelInfoCrudRepository


    @Autowired
    private TunnelImportPortCrudRepository tunnelImportPortCrudRepository
    @Autowired
    private TunnelImportPortRepository tunnelImportPortRepository


    @Autowired
    private AssistTunnelCrudRepository assistTunnelCrudRepository
    @Autowired
    private AssistTunnelRepository assistTunnelRepository
    @Autowired
    private EnvirRiskExamineCrudRepository envirRiskExamineCrudRepository
    @Autowired
    private EnvirRiskExamineRepository envirRiskExamineRepository
    @Autowired
    private FigureCrudRepository figureCrudRepository
    @Autowired
    private FigureRepository figureRepository
    @Autowired
    private FireRiskExmaineCrudRepository fireRiskExmaineCrudRepository
    @Autowired
    private FireRiskExmaineRepository fireRiskExmaineRepository
    @Autowired
    private GasRiskExamineCrudRepository gasRiskExamineCrudRepository
    @Autowired
    private GasRiskExamineRepository gasRiskExamineRepository
    @Autowired
    private OtherRiskExamineCrudRepository otherRiskExamineCrudRepository
    @Autowired
    private OtherRiskExamineRepository otherRiskExamineRepository
    @Autowired
    private OverBreakRiskExmaineCrudRepository overBreakRiskExmaineCrudRepository
    @Autowired
    private OverBreakRiskExmaineRepository overBreakRiskExmaineRepository
    @Autowired
    private RockOutburstRiskExamineCrudRepository rockOutburstRiskExamineCrudRepository
    @Autowired
    private RockOutburstRiskExamineRepository rockOutburstRiskExamineRepository
    @Autowired
    private ShapeRiskExamineCrudRepository shapeRiskExamineCrudRepository
    @Autowired
    private ShapeRiskExamineRepository shapeRiskExamineRepository
    @Autowired
    private SurgeMudRiskExamineCrudRepository surgeMudRiskExamineCrudRepository
    @Autowired
    private SurgeMudRiskExamineRepository surgeMudRiskExamineRepository
    @Autowired
    private TrafficAccidentRiskExamineCrudRepository trafficAccidentRiskExamineCrudRepository
    @Autowired
    private TrafficAccidentRiskExamineRepository trafficAccidentRiskExamineRepository
    @Autowired
    private TunnelGrabageExamineCrudRepository tunnelGrabageExamineCrudRepository
    @Autowired
    private TunnelGrabageExamineRepository tunnelGrabageExamineRepository
    @Autowired
    private TunnelHeadRiskExamineCrudRepository tunnelHeadRiskExamineCrudRepository
    @Autowired
    private TunnelHeadRiskExamineRepository tunnelHeadRiskExamineRepository
    @Autowired
    private TunnelShallowCoverCrudRepository tunnelShallowCoverCrudRepository
    @Autowired
    private TunnelShallowCoverRepository tunnelShallowCoverRepository


    @Autowired
    private TunnelExmainRelationQueryCrudRepository tunnelExmainRelationQueryCrudRepository

    @Override
    void saveTunnelExmaineRelationQuery(TunnelExamineRelationQuery tunnelExamineRelationQuery) {
        tunnelExmainRelationQueryCrudRepository.save(tunnelExamineRelationQuery)
    }

    void saveBaseDomain(BaseDomain baseDomain) {

        if (baseDomain instanceof User) {
            userCrudRepository.save(baseDomain)
        } else if (baseDomain instanceof Project) {
            projectCrudRepository.save(baseDomain)
        } else if (baseDomain instanceof TunnelInfo) {
            tunnelInfoCrudRepository.save(baseDomain)
        } else if (baseDomain instanceof TunnelImportPort) {
            tunnelImportPortCrudRepository.save(baseDomain)

        } else if (baseDomain instanceof AssistTunnel) {
            assistTunnelCrudRepository.save(baseDomain)
        } else if (baseDomain instanceof EnvirRiskExamine) {
            envirRiskExamineCrudRepository.save(baseDomain)
        } else if (baseDomain instanceof Figure) {
            figureCrudRepository.save(baseDomain)
        } else if (baseDomain instanceof FireRiskExmaine) {
            fireRiskExmaineCrudRepository.save(baseDomain)
        } else if (baseDomain instanceof GasRiskExamine) {
            gasRiskExamineCrudRepository.save(baseDomain)
        } else if (baseDomain instanceof OtherRiskExamine) {
            otherRiskExamineCrudRepository.save(baseDomain)
        } else if (baseDomain instanceof OverBreakRiskExmaine) {
            overBreakRiskExmaineCrudRepository.save(baseDomain)
        } else if (baseDomain instanceof RockOutburstRiskExamine) {
            rockOutburstRiskExamineCrudRepository.save(baseDomain)
        } else if (baseDomain instanceof ShapeRiskExamine) {
            shapeRiskExamineCrudRepository.save(baseDomain)
        } else if (baseDomain instanceof SurgeMudRiskExamine) {
            surgeMudRiskExamineCrudRepository.save(baseDomain)
        } else if (baseDomain instanceof TrafficAccidentRiskExamine) {
            trafficAccidentRiskExamineCrudRepository.save(baseDomain)
        } else if (baseDomain instanceof TunnelGrabageExamine) {
            tunnelGrabageExamineCrudRepository.save(baseDomain)
        } else if (baseDomain instanceof TunnelHeadRiskExamine) {
            tunnelHeadRiskExamineCrudRepository.save(baseDomain)
        } else if (baseDomain instanceof TunnelShallowCover) {
            tunnelShallowCoverCrudRepository.save(baseDomain)
        }

    }

    @Override
    void saveBaseDomainAll(List<BaseDomain> baseDomains) {
        if (baseDomains == null || baseDomains.isEmpty()) {
            return
        }
        BaseDomain baseDomain = baseDomains.get(0)
        if (baseDomain instanceof User) {
            userCrudRepository.save(baseDomains)
        } else if (baseDomain instanceof Project) {
            projectCrudRepository.save(baseDomains)
        } else if (baseDomain instanceof TunnelInfo) {
            tunnelInfoCrudRepository.save(baseDomains)
        } else if (baseDomain instanceof TunnelImportPort) {
            tunnelImportPortCrudRepository.save(baseDomains)

        } else if (baseDomain instanceof AssistTunnel) {
            assistTunnelCrudRepository.save(baseDomains)
        } else if (baseDomain instanceof EnvirRiskExamine) {
            envirRiskExamineCrudRepository.save(baseDomains)
        } else if (baseDomain instanceof Figure) {
            figureCrudRepository.save(baseDomains)
        } else if (baseDomain instanceof FireRiskExmaine) {
            fireRiskExmaineCrudRepository.save(baseDomains)
        } else if (baseDomain instanceof GasRiskExamine) {
            gasRiskExamineCrudRepository.save(baseDomains)
        } else if (baseDomain instanceof OtherRiskExamine) {
            otherRiskExamineCrudRepository.save(baseDomains)
        } else if (baseDomain instanceof OverBreakRiskExmaine) {
            overBreakRiskExmaineCrudRepository.save(baseDomains)
        } else if (baseDomain instanceof RockOutburstRiskExamine) {
            rockOutburstRiskExamineCrudRepository.save(baseDomains)
        } else if (baseDomain instanceof ShapeRiskExamine) {
            shapeRiskExamineCrudRepository.save(baseDomains)
        } else if (baseDomain instanceof SurgeMudRiskExamine) {
            surgeMudRiskExamineCrudRepository.save(baseDomains)
        } else if (baseDomain instanceof TrafficAccidentRiskExamine) {
            trafficAccidentRiskExamineCrudRepository.save(baseDomains)
        } else if (baseDomain instanceof TunnelGrabageExamine) {
            tunnelGrabageExamineCrudRepository.save(baseDomains)
        } else if (baseDomain instanceof TunnelHeadRiskExamine) {
            tunnelHeadRiskExamineCrudRepository.save(baseDomains)
        } else if (baseDomain instanceof TunnelShallowCover) {
            tunnelShallowCoverCrudRepository.save(baseDomains)
        }

    }

    BaseDomain findBaseDomainByBaseDomain(BaseDomain baseDomain) {

        if (baseDomain instanceof User) {
            User user = (User) baseDomain

            if (user.getId() != null) {
                return userCrudRepository.findOne(user.getId())
            } else {
                return userCrudRepository.findByName(user.getName())

            }

        } else if (baseDomain instanceof Project) {


            Project project = (Project) baseDomain

            if (project.getId() != null) {

                return projectCrudRepository.findOne(project.getId())
            } else {

                return projectCrudRepository.findByProjectNumber(project.getProjectNumber())

            }


        } else if (baseDomain instanceof TunnelInfo) {


            TunnelInfo tunnelInfo = (TunnelInfo) baseDomain

            if (tunnelInfo.getId() != null) {

                return tunnelInfoCrudRepository.findOne(tunnelInfo.getId())
            } else {

                return tunnelInfoCrudRepository.findByTunnelNumber(tunnelInfo.getTunnelNumber())

            }


        } else if (baseDomain instanceof TunnelImportPort) {
            TunnelImportPort tunnelImportPort = (TunnelImportPort) baseDomain

            return tunnelImportPortCrudRepository.findOne(tunnelImportPort.getId())


        } else if (baseDomain instanceof AssistTunnel) {
            AssistTunnel assistTunnel = (AssistTunnel) baseDomain
            return assistTunnelCrudRepository.findOne(assistTunnel.getId())
        } else if (baseDomain instanceof EnvirRiskExamine) {
            EnvirRiskExamine envirRiskExamine = (EnvirRiskExamine) baseDomain
            return envirRiskExamineCrudRepository.findOne(envirRiskExamine.getId())
        } else if (baseDomain instanceof Figure) {
            Figure figure = (Figure) baseDomain
            return figureCrudRepository.findOne(figure.getId())
        } else if (baseDomain instanceof FireRiskExmaine) {
            FireRiskExmaine fireRiskExmaine = (FireRiskExmaine) baseDomain
            return fireRiskExmaineCrudRepository.findOne(fireRiskExmaine.getId())
        } else if (baseDomain instanceof GasRiskExamine) {
            GasRiskExamine gasRiskExamine = (GasRiskExamine) baseDomain
            return gasRiskExamineCrudRepository.findOne(gasRiskExamine.getId())
        } else if (baseDomain instanceof OtherRiskExamine) {
            OtherRiskExamine otherRiskExamine = (OtherRiskExamine) baseDomain
            return otherRiskExamineCrudRepository.findOne(otherRiskExamine.getId())
        } else if (baseDomain instanceof OverBreakRiskExmaine) {
            OverBreakRiskExmaine overBreakRiskExmaine = (OverBreakRiskExmaine) baseDomain
            return overBreakRiskExmaineCrudRepository.findOne(overBreakRiskExmaine.getId())
        } else if (baseDomain instanceof RockOutburstRiskExamine) {
            RockOutburstRiskExamine rockOutburstRiskExamine = (RockOutburstRiskExamine) baseDomain
            return rockOutburstRiskExamineCrudRepository.findOne(rockOutburstRiskExamine.getId())
        } else if (baseDomain instanceof ShapeRiskExamine) {
            ShapeRiskExamine shapeRiskExamine = (ShapeRiskExamine) baseDomain
            return shapeRiskExamineCrudRepository.findOne(shapeRiskExamine.getId())
        } else if (baseDomain instanceof SurgeMudRiskExamine) {
            SurgeMudRiskExamine surgeMudRiskExamine = (SurgeMudRiskExamine) baseDomain
            return surgeMudRiskExamineCrudRepository.findOne(surgeMudRiskExamine.getId())
        } else if (baseDomain instanceof TrafficAccidentRiskExamine) {
            TrafficAccidentRiskExamine trafficAccidentRiskExamine = (TrafficAccidentRiskExamine) baseDomain
            return trafficAccidentRiskExamineCrudRepository.findOne(trafficAccidentRiskExamine.getId())
        } else if (baseDomain instanceof TunnelGrabageExamine) {
            TunnelGrabageExamine tunnelGrabageExamine = (TunnelGrabageExamine) baseDomain
            return tunnelGrabageExamineCrudRepository.findOne(tunnelGrabageExamine.getId())
        } else if (baseDomain instanceof TunnelHeadRiskExamine) {
            TunnelHeadRiskExamine tunnelHeadRiskExamine = (TunnelHeadRiskExamine) baseDomain
            return tunnelHeadRiskExamineCrudRepository.findOne(tunnelHeadRiskExamine.getId())
        } else if (baseDomain instanceof TunnelShallowCover) {
            TunnelShallowCover tunnelShallowCover = (TunnelShallowCover) baseDomain
            return tunnelShallowCoverCrudRepository.findOne(tunnelShallowCover.getId())
        } else if (baseDomain instanceof TunnelExamineRelationQuery) {

            TunnelExamineRelationQuery tunnelExamineRelationQuery = (TunnelExamineRelationQuery) baseDomain

            return tunnelExmainRelationQueryCrudRepository.findByTunnelNumberAndTableName(tunnelExamineRelationQuery.getTunnelNumber(), tunnelExamineRelationQuery.getTableName())

        }
        return baseDomain
    }

    @Override
    List<BaseDomain> findBaseDomainALlByPage(BaseDomain baseDomain) {

        if (baseDomain instanceof User) {
            User user = (User) baseDomain
            if (StringUtils.isEmpty(user.getName()) && user.getPage() != null) {
                return userRepository.findUssrALlByPage(user.getPage(), BaseDomain.pageSize)
            } else if (user.getPage() != null) {
                return userRepository.findUserALlByNameAndPage(user.getPage(), BaseDomain.pageSize, user.getName())

            }

        } else if (baseDomain instanceof Project) {
            Project project = (Project) baseDomain
            if (StringUtils.isEmpty(project.getProjectNumber()) && project.getPage() != null) {
                return projectRepository.findProjectALl(project.getPage(), BaseDomain.pageSize)

            } else if (project.getPage() != null) {
                return projectRepository.findProjectALlByNumberAndPage(project.getPage(), BaseDomain.pageSize, project.getProjectNumber())
            } else {
                return projectCrudRepository.findAll()
            }
        } else if (baseDomain instanceof TunnelInfo) {
            TunnelInfo tunnelInfo = (TunnelInfo) baseDomain
            if (StringUtils.isEmpty(tunnelInfo.getTunnelNumber()) && tunnelInfo.getPage() != null) {
                return tunnelInfoRepository.findTunnelInfoALl(tunnelInfo.getPage(), BaseDomain.pageSize)

            } else if (tunnelInfo.getPage() != null) {
                return tunnelInfoRepository.findTunnelInfoALlByNumberAndPage(tunnelInfo.getPage(), BaseDomain.pageSize, tunnelInfo.getTunnelNumber())
            } else {
                return tunnelInfoRepository.findTunnelInfoALlByNumber(tunnelInfo.getProjectNumber())
            }
        } else if (baseDomain instanceof TunnelImportPort) {
            TunnelImportPort tunnelImportPort = (TunnelImportPort) baseDomain
            return tunnelImportPortRepository.findTunnelImportPortALlByNumberAndPage(tunnelImportPort.getPage(), BaseDomain.pageSize, tunnelImportPort.getTunnelNumber())

        } else if (baseDomain instanceof AssistTunnel) {
            AssistTunnel assistTunnel = (AssistTunnel) baseDomain
            return assistTunnelRepository.findAssistTunnelALlByNumberAndPage(assistTunnel.getPage(), BaseDomain.pageSize, assistTunnel.getTunnelNumber())

        } else if (baseDomain instanceof EnvirRiskExamine) {
            EnvirRiskExamine envirRiskExamine = (EnvirRiskExamine) baseDomain
            return envirRiskExamineRepository.findEnvirRiskExamineALlByNumberAndPage(envirRiskExamine.getPage(), BaseDomain.pageSize, envirRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof Figure) {
            Figure figure = (Figure) baseDomain
            return figureRepository.findFigureALlByNumberAndPage(figure.getPage(), BaseDomain.pageSize, figure.getTunnelNumber())

        } else if (baseDomain instanceof FireRiskExmaine) {
            FireRiskExmaine fireRiskExmaine = (FireRiskExmaine) baseDomain
            return fireRiskExmaineRepository.findFireRiskExmaineALlByNumberAndPage(fireRiskExmaine.getPage(), BaseDomain.pageSize, fireRiskExmaine.getTunnelNumber())

        } else if (baseDomain instanceof GasRiskExamine) {
            GasRiskExamine gasRiskExamine = (GasRiskExamine) baseDomain
            return gasRiskExamineRepository.findGasRiskExamineALlByNumberAndPage(gasRiskExamine.getPage(), BaseDomain.pageSize, gasRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof OtherRiskExamine) {
            OtherRiskExamine otherRiskExamine = (OtherRiskExamine) baseDomain
            return otherRiskExamineRepository.findOtherRiskExamineALlByNumberAndPage(otherRiskExamine.getPage(), BaseDomain.pageSize, otherRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof OverBreakRiskExmaine) {
            OverBreakRiskExmaine overBreakRiskExmaine = (OverBreakRiskExmaine) baseDomain
            return overBreakRiskExmaineRepository.findOverBreakRiskExmaineALlByNumberAndPage(overBreakRiskExmaine.getPage(), BaseDomain.pageSize, overBreakRiskExmaine.getTunnelNumber())

        } else if (baseDomain instanceof RockOutburstRiskExamine) {
            RockOutburstRiskExamine rockOutburstRiskExamine = (RockOutburstRiskExamine) baseDomain
            return rockOutburstRiskExamineRepository.findRockOutburstRiskExamineALlByNumberAndPage(rockOutburstRiskExamine.getPage(), BaseDomain.pageSize, rockOutburstRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof ShapeRiskExamine) {
            ShapeRiskExamine shapeRiskExamine = (ShapeRiskExamine) baseDomain
            return shapeRiskExamineRepository.findShapeRiskExamineALlByNumberAndPage(shapeRiskExamine.getPage(), BaseDomain.pageSize, shapeRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof SurgeMudRiskExamine) {
            SurgeMudRiskExamine surgeMudRiskExamine = (SurgeMudRiskExamine) baseDomain
            return surgeMudRiskExamineRepository.findSurgeMudRiskExamineALlByNumberAndPage(surgeMudRiskExamine.getPage(), BaseDomain.pageSize, surgeMudRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof TrafficAccidentRiskExamine) {
            TrafficAccidentRiskExamine trafficAccidentRiskExamine = (TrafficAccidentRiskExamine) baseDomain
            return trafficAccidentRiskExamineRepository.findTrafficAccidentRiskExamineALlByNumberAndPage(trafficAccidentRiskExamine.getPage(), BaseDomain.pageSize, trafficAccidentRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof TunnelGrabageExamine) {
            TunnelGrabageExamine tunnelGrabageExamine = (TunnelGrabageExamine) baseDomain
            return tunnelGrabageExamineRepository.findTunnelGrabageExamineALlByNumberAndPage(tunnelGrabageExamine.getPage(), BaseDomain.pageSize, tunnelGrabageExamine.getTunnelNumber())

        } else if (baseDomain instanceof TunnelHeadRiskExamine) {
            TunnelHeadRiskExamine tunnelHeadRiskExamine = (TunnelHeadRiskExamine) baseDomain
            return tunnelHeadRiskExamineRepository.findTunnelHeadRiskExamineALlByNumberAndPage(tunnelHeadRiskExamine.getPage(), BaseDomain.pageSize, tunnelHeadRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof TunnelShallowCover) {
            TunnelShallowCover tunnelShallowCover = (TunnelShallowCover) baseDomain
            return tunnelShallowCoverRepository.findTunnelShallowCoverALlByNumberAndPage(tunnelShallowCover.getPage(), BaseDomain.pageSize, tunnelShallowCover.getTunnelNumber())

        } else if (baseDomain instanceof TunnelExamineRelationQuery) {
            TunnelExamineRelationQuery tunnelExamineRelationQuery = (TunnelExamineRelationQuery) baseDomain

            return tunnelExmainRelationQueryCrudRepository.findByTunnelNumber(tunnelExamineRelationQuery.getTunnelNumber())
        }
        return new ArrayList<BaseDomain>()

    }

    void updateBaseDomainByBaseDomain(BaseDomain baseDomain) {
        if (baseDomain instanceof User) {
            User user = (User) baseDomain

            if (user.getId() != null) {
                userRepository.updateUserAllByLoadTimeAndName(user.getRealName(), user.getName(), user.getPassword(), user.getAuthorLevel(), user.getPassKey(), user.getStatus(), user.getId())

            } else {
                userRepository.updateUserPassByLoadTimeAndName(user.getName(), user.getLoadTime())

            }


        } else if (baseDomain instanceof Project) {
            Project project = (Project) baseDomain
            if (project.getId() != null) {
                projectRepository.updateProjectInfoByProject(project.getProjectNumber(), project.getProjectName(), project.getExaminePersion(), project.getContactInfo(), project.getExamineDate(), project.getId())
            }

        } else if (baseDomain instanceof TunnelInfo) {
            TunnelInfo tunnelInfo = (TunnelInfo) baseDomain
            if (tunnelInfo.getId() != null) {
                tunnelInfoCrudRepository.delete(tunnelInfo.getId())
                tunnelInfoCrudRepository.save(tunnelInfo)
            }

        } else if (baseDomain instanceof TunnelImportPort) {
            TunnelImportPort tunnelImportPort = (TunnelImportPort) baseDomain
            if (tunnelImportPort.getId() != null) {
                tunnelImportPortCrudRepository.delete(tunnelImportPort.getId())
                tunnelImportPortCrudRepository.save(tunnelImportPort)
            }
        } else if (baseDomain instanceof AssistTunnel) {
            AssistTunnel assistTunnel = (AssistTunnel) baseDomain
            if (assistTunnel.getId() != null && assistTunnel.getCreateime() == null) {
                assistTunnelCrudRepository.delete(assistTunnel.getId())
                assistTunnelCrudRepository.save(assistTunnel)
            } else if (assistTunnel.getId() != null && assistTunnel.getCreateime() != null) {
                assistTunnelRepository.updateAcccessTunnelAndAirshaftRelationWithMainTunnelPictureByAcccessTunnelAndAirshaftRelationWithMainTunnel(assistTunnel.getAcccessTunnelAndAirshaftRelationWithMainTunnel(), assistTunnel.getId())
            }


        } else if (baseDomain instanceof EnvirRiskExamine) {
            EnvirRiskExamine envirRiskExamine = (EnvirRiskExamine) baseDomain
            if (envirRiskExamine.getId() != null) {
                envirRiskExamineCrudRepository.delete(envirRiskExamine.getId())
                envirRiskExamineCrudRepository.save(envirRiskExamine)
            }
        } else if (baseDomain instanceof Figure) {
            Figure figure = (Figure) baseDomain
            if (figure.getId() != null && figure.getCreateime() == null) {
                figureCrudRepository.delete(figure.getId())
                figureCrudRepository.save(figure)
            } else if (figure.getId() != null && figure.getCreateime() != null) {
                figureRepository.updateFigurePhotosPictureByPhotos(figure.getPhotosPicture(), figure.getId())
            }
        } else if (baseDomain instanceof FireRiskExmaine) {
            FireRiskExmaine fireRiskExmaine = (FireRiskExmaine) baseDomain
            if (fireRiskExmaine.getId() != null) {
                fireRiskExmaineCrudRepository.delete(fireRiskExmaine.getId())
                fireRiskExmaineCrudRepository.save(fireRiskExmaine)
            }
        } else if (baseDomain instanceof GasRiskExamine) {
            GasRiskExamine gasRiskExamine = (GasRiskExamine) baseDomain
            if (gasRiskExamine.getId() != null) {
                gasRiskExamineCrudRepository.delete(gasRiskExamine.getId())
                gasRiskExamineCrudRepository.save(gasRiskExamine)
            }
        } else if (baseDomain instanceof OtherRiskExamine) {
            OtherRiskExamine otherRiskExamine = (OtherRiskExamine) baseDomain
            if (otherRiskExamine.getId() != null) {
                otherRiskExamineCrudRepository.delete(otherRiskExamine.getId())
                otherRiskExamineCrudRepository.save(otherRiskExamine)
            }
        } else if (baseDomain instanceof OverBreakRiskExmaine) {
            OverBreakRiskExmaine overBreakRiskExmaine = (OverBreakRiskExmaine) baseDomain
            if (overBreakRiskExmaine.getId() != null) {
                overBreakRiskExmaineCrudRepository.delete(overBreakRiskExmaine.getId())
                overBreakRiskExmaineCrudRepository.save(overBreakRiskExmaine)
            }
        } else if (baseDomain instanceof RockOutburstRiskExamine) {
            RockOutburstRiskExamine rockOutburstRiskExamine = (RockOutburstRiskExamine) baseDomain
            if (rockOutburstRiskExamine.getId() != null) {
                rockOutburstRiskExamineCrudRepository.delete(rockOutburstRiskExamine.getId())
                rockOutburstRiskExamineCrudRepository.save(rockOutburstRiskExamine)
            }
        } else if (baseDomain instanceof ShapeRiskExamine) {
            ShapeRiskExamine shapeRiskExamine = (ShapeRiskExamine) baseDomain
            if (shapeRiskExamine.getId() != null) {
                shapeRiskExamineCrudRepository.delete(shapeRiskExamine.getId())
                shapeRiskExamineCrudRepository.save(shapeRiskExamine)
            }
        } else if (baseDomain instanceof SurgeMudRiskExamine) {
            SurgeMudRiskExamine surgeMudRiskExamine = (SurgeMudRiskExamine) baseDomain
            if (surgeMudRiskExamine.getId() != null) {
                surgeMudRiskExamineCrudRepository.delete(surgeMudRiskExamine.getId())
                surgeMudRiskExamineCrudRepository.save(surgeMudRiskExamine)
            }
        } else if (baseDomain instanceof TrafficAccidentRiskExamine) {
            TrafficAccidentRiskExamine trafficAccidentRiskExamine = (TrafficAccidentRiskExamine) baseDomain
            if (trafficAccidentRiskExamine.getId() != null) {
                trafficAccidentRiskExamineCrudRepository.delete(trafficAccidentRiskExamine.getId())
                trafficAccidentRiskExamineCrudRepository.save(trafficAccidentRiskExamine)
            }
        } else if (baseDomain instanceof TunnelGrabageExamine) {
            TunnelGrabageExamine tunnelGrabageExamine = (TunnelGrabageExamine) baseDomain
            if (tunnelGrabageExamine.getId() != null) {
                tunnelGrabageExamineCrudRepository.delete(tunnelGrabageExamine.getId())
                tunnelGrabageExamineCrudRepository.save(tunnelGrabageExamine)
            }
        } else if (baseDomain instanceof TunnelHeadRiskExamine) {
            TunnelHeadRiskExamine tunnelHeadRiskExamine = (TunnelHeadRiskExamine) baseDomain
            if (tunnelHeadRiskExamine.getId() != null) {
                tunnelHeadRiskExamineCrudRepository.delete(tunnelHeadRiskExamine.getId())
                tunnelHeadRiskExamineCrudRepository.save(tunnelHeadRiskExamine)
            }
        } else if (baseDomain instanceof TunnelShallowCover) {
            TunnelShallowCover tunnelShallowCover = (TunnelShallowCover) baseDomain
            if (tunnelShallowCover.getId() != null) {
                tunnelShallowCoverCrudRepository.delete(tunnelShallowCover.getId())
                tunnelShallowCoverCrudRepository.save(tunnelShallowCover)
            }
        }

    }

    void deleteBaseDomainById(BaseDomain baseDomain) {
        if (baseDomain instanceof User) {
            User user = (User) baseDomain

            userCrudRepository.delete(user.getId())

        } else if (baseDomain instanceof Project) {
            Project project = (Project) baseDomain
            projectCrudRepository.delete(project.getId())


        } else if (baseDomain instanceof TunnelInfo) {
            TunnelInfo tunnelInfo = (TunnelInfo) baseDomain
            tunnelInfoCrudRepository.delete(tunnelInfo.getId())


        } else if (baseDomain instanceof TunnelExamineRelationQuery) {
            TunnelExamineRelationQuery tunnelExamineRelationQuery = (TunnelExamineRelationQuery) baseDomain
            tunnelExmainRelationQueryCrudRepository.delete(tunnelExamineRelationQuery.getId())

        } else if (baseDomain instanceof TunnelImportPort) {
            TunnelImportPort tunnelImportPort = (TunnelImportPort) baseDomain
            tunnelImportPortCrudRepository.delete(tunnelImportPort.getId())
        } else if (baseDomain instanceof AssistTunnel) {
            AssistTunnel assistTunnel = (AssistTunnel) baseDomain
            assistTunnelCrudRepository.delete(assistTunnel.getId())
        } else if (baseDomain instanceof EnvirRiskExamine) {
            EnvirRiskExamine envirRiskExamine = (EnvirRiskExamine) baseDomain
            envirRiskExamineCrudRepository.delete(envirRiskExamine.getId())
        } else if (baseDomain instanceof Figure) {
            Figure figure = (Figure) baseDomain
            figureCrudRepository.delete(figure.getId())
        } else if (baseDomain instanceof FireRiskExmaine) {
            FireRiskExmaine fireRiskExmaine = (FireRiskExmaine) baseDomain
            fireRiskExmaineCrudRepository.delete(fireRiskExmaine.getId())
        } else if (baseDomain instanceof GasRiskExamine) {
            GasRiskExamine gasRiskExamine = (GasRiskExamine) baseDomain
            gasRiskExamineCrudRepository.delete(gasRiskExamine.getId())
        } else if (baseDomain instanceof OtherRiskExamine) {
            OtherRiskExamine otherRiskExamine = (OtherRiskExamine) baseDomain
            otherRiskExamineCrudRepository.delete(otherRiskExamine.getId())
        } else if (baseDomain instanceof OverBreakRiskExmaine) {
            OverBreakRiskExmaine overBreakRiskExmaine = (OverBreakRiskExmaine) baseDomain
            overBreakRiskExmaineCrudRepository.delete(overBreakRiskExmaine.getId())
        } else if (baseDomain instanceof RockOutburstRiskExamine) {
            RockOutburstRiskExamine rockOutburstRiskExamine = (RockOutburstRiskExamine) baseDomain
            rockOutburstRiskExamineCrudRepository.delete(rockOutburstRiskExamine.getId())
        } else if (baseDomain instanceof ShapeRiskExamine) {
            ShapeRiskExamine shapeRiskExamine = (ShapeRiskExamine) baseDomain
            shapeRiskExamineCrudRepository.delete(shapeRiskExamine.getId())
        } else if (baseDomain instanceof SurgeMudRiskExamine) {
            SurgeMudRiskExamine surgeMudRiskExamine = (SurgeMudRiskExamine) baseDomain
            surgeMudRiskExamineCrudRepository.delete(surgeMudRiskExamine.getId())
        } else if (baseDomain instanceof TrafficAccidentRiskExamine) {
            TrafficAccidentRiskExamine trafficAccidentRiskExamine = (TrafficAccidentRiskExamine) baseDomain
            trafficAccidentRiskExamineCrudRepository.delete(trafficAccidentRiskExamine.getId())
        } else if (baseDomain instanceof TunnelGrabageExamine) {
            TunnelGrabageExamine tunnelGrabageExamine = (TunnelGrabageExamine) baseDomain
            tunnelGrabageExamineCrudRepository.delete(tunnelGrabageExamine.getId())
        } else if (baseDomain instanceof TunnelHeadRiskExamine) {
            TunnelHeadRiskExamine tunnelHeadRiskExamine = (TunnelHeadRiskExamine) baseDomain
            tunnelHeadRiskExamineCrudRepository.delete(tunnelHeadRiskExamine.getId())
        } else if (baseDomain instanceof TunnelShallowCover) {
            TunnelShallowCover tunnelShallowCover = (TunnelShallowCover) baseDomain
            tunnelShallowCoverCrudRepository.delete(tunnelShallowCover.getId())
        }
    }

    Integer findBaseDomainCountByBaseDomain(BaseDomain baseDomain) {
        if (baseDomain == null) {
            return 0
        }
        if (baseDomain instanceof User) {
            User user = (User) baseDomain

            if (StringUtils.isEmpty(user.getName())) {
                return userCrudRepository.count()

            } else {
                return userRepository.findUserCountALlByNameAndPage(user.getName())
            }

        } else if (baseDomain instanceof Project) {
            Project project = (Project) baseDomain
            if (StringUtils.isEmpty(project.getProjectNumber())) {
                return projectCrudRepository.count()

            } else {
                return projectRepository.findProjectCountALlByNumberAndPage(project.getProjectNumber())
            }
        } else if (baseDomain instanceof TunnelInfo) {
            TunnelInfo tunnelInfo = (TunnelInfo) baseDomain
            if (StringUtils.isEmpty(tunnelInfo.getTunnelNumber())) {
                return tunnelInfoCrudRepository.count()

            } else {
                return tunnelInfoRepository.findTunnelInfoCountALlByNumberAndPage(tunnelInfo.getTunnelNumber())
            }
        } else if (baseDomain instanceof TunnelImportPort) {
            TunnelImportPort tunnelImportPort = (TunnelImportPort) baseDomain
            return tunnelImportPortRepository.findTunnelImportPortCountALlByNumberAndPage(tunnelImportPort.getTunnelNumber())

        } else if (baseDomain instanceof AssistTunnel) {
            AssistTunnel assistTunnel = (AssistTunnel) baseDomain
            return assistTunnelRepository.findAssistTunnelCountALlByNumberAndPage(assistTunnel.getTunnelNumber())

        } else if (baseDomain instanceof EnvirRiskExamine) {
            EnvirRiskExamine envirRiskExamine = (EnvirRiskExamine) baseDomain
            return envirRiskExamineRepository.findEnvirRiskExamineCountALlByNumberAndPage(envirRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof Figure) {
            Figure figure = (Figure) baseDomain
            return figureRepository.findFigureCountALlByNumberAndPage(figure.getTunnelNumber())

        } else if (baseDomain instanceof FireRiskExmaine) {
            FireRiskExmaine fireRiskExmaine = (FireRiskExmaine) baseDomain
            return fireRiskExmaineRepository.findFireRiskExmaineCountALlByNumberAndPage(fireRiskExmaine.getTunnelNumber())

        } else if (baseDomain instanceof GasRiskExamine) {
            GasRiskExamine gasRiskExamine = (GasRiskExamine) baseDomain
            return gasRiskExamineRepository.findGasRiskExamineCountALlByNumberAndPage(gasRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof OtherRiskExamine) {
            OtherRiskExamine otherRiskExamine = (OtherRiskExamine) baseDomain
            return otherRiskExamineRepository.findOtherRiskExamineCountALlByNumberAndPage(otherRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof OverBreakRiskExmaine) {
            OverBreakRiskExmaine overBreakRiskExmaine = (OverBreakRiskExmaine) baseDomain
            return overBreakRiskExmaineRepository.findOverBreakRiskExmaineCountALlByNumberAndPage(overBreakRiskExmaine.getTunnelNumber())

        } else if (baseDomain instanceof RockOutburstRiskExamine) {
            RockOutburstRiskExamine rockOutburstRiskExamine = (RockOutburstRiskExamine) baseDomain
            return rockOutburstRiskExamineRepository.findRockOutburstRiskExamineCountALlByNumberAndPage(rockOutburstRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof ShapeRiskExamine) {
            ShapeRiskExamine shapeRiskExamine = (ShapeRiskExamine) baseDomain
            return shapeRiskExamineRepository.findShapeRiskExamineCountALlByNumberAndPage(shapeRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof SurgeMudRiskExamine) {
            SurgeMudRiskExamine surgeMudRiskExamine = (SurgeMudRiskExamine) baseDomain
            return surgeMudRiskExamineRepository.findSurgeMudRiskExamineCountALlByNumberAndPage(surgeMudRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof TrafficAccidentRiskExamine) {
            TrafficAccidentRiskExamine trafficAccidentRiskExamine = (TrafficAccidentRiskExamine) baseDomain
            return trafficAccidentRiskExamineRepository.findTrafficAccidentRiskExamineCountALlByNumberAndPage(trafficAccidentRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof TunnelGrabageExamine) {
            TunnelGrabageExamine tunnelGrabageExamine = (TunnelGrabageExamine) baseDomain
            return tunnelGrabageExamineRepository.findTunnelGrabageExamineCountALlByNumberAndPage(tunnelGrabageExamine.getTunnelNumber())

        } else if (baseDomain instanceof TunnelHeadRiskExamine) {
            TunnelHeadRiskExamine tunnelHeadRiskExamine = (TunnelHeadRiskExamine) baseDomain
            return tunnelHeadRiskExamineRepository.findTunnelHeadRiskExamineCountALlByNumberAndPage(tunnelHeadRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof TunnelShallowCover) {
            TunnelShallowCover tunnelShallowCover = (TunnelShallowCover) baseDomain
            return tunnelShallowCoverRepository.findTunnelShallowCoverCountALlByNumberAndPage(tunnelShallowCover.getTunnelNumber())

        }
        return 0
    }

    @Override
    List<BaseDomain> findBaseDomainALl(BaseDomain baseDomain) {
        if (baseDomain instanceof User) {
            User user = (User) baseDomain

            return userRepository.findUssrALlByPage(user.getPage(), Integer.MAX_VALUE)


        } else if (baseDomain instanceof Project) {
            Project project = (Project) baseDomain

            return projectRepository.findProjectALl(project.getPage(), Integer.MAX_VALUE)


        } else if (baseDomain instanceof TunnelInfo) {
            TunnelInfo tunnelInfo = (TunnelInfo) baseDomain

            return tunnelInfoRepository.findTunnelInfoALl(tunnelInfo.getPage(), Integer.MAX_VALUE)

        } else if (baseDomain instanceof TunnelImportPort) {
            TunnelImportPort tunnelImportPort = (TunnelImportPort) baseDomain
            return tunnelImportPortRepository.findTunnelImportPortALlByNumberAndPage(tunnelImportPort.getPage(), Integer.MAX_VALUE, tunnelImportPort.getTunnelNumber())

        } else if (baseDomain instanceof AssistTunnel) {
            AssistTunnel assistTunnel = (AssistTunnel) baseDomain
            return assistTunnelRepository.findAssistTunnelALlByNumberAndPage(assistTunnel.getPage(), Integer.MAX_VALUE, assistTunnel.getTunnelNumber())

        } else if (baseDomain instanceof EnvirRiskExamine) {
            EnvirRiskExamine envirRiskExamine = (EnvirRiskExamine) baseDomain
            return envirRiskExamineRepository.findEnvirRiskExamineALlByNumberAndPage(envirRiskExamine.getPage(), Integer.MAX_VALUE, envirRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof Figure) {
            Figure figure = (Figure) baseDomain
            return figureRepository.findFigureALlByNumberAndPage(figure.getPage(), Integer.MAX_VALUE, figure.getTunnelNumber())

        } else if (baseDomain instanceof FireRiskExmaine) {
            FireRiskExmaine fireRiskExmaine = (FireRiskExmaine) baseDomain
            return fireRiskExmaineRepository.findFireRiskExmaineALlByNumberAndPage(fireRiskExmaine.getPage(), Integer.MAX_VALUE, fireRiskExmaine.getTunnelNumber())

        } else if (baseDomain instanceof GasRiskExamine) {
            GasRiskExamine gasRiskExamine = (GasRiskExamine) baseDomain
            return gasRiskExamineRepository.findGasRiskExamineALlByNumberAndPage(gasRiskExamine.getPage(), Integer.MAX_VALUE, gasRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof OtherRiskExamine) {
            OtherRiskExamine otherRiskExamine = (OtherRiskExamine) baseDomain
            return otherRiskExamineRepository.findOtherRiskExamineALlByNumberAndPage(otherRiskExamine.getPage(), Integer.MAX_VALUE, otherRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof OverBreakRiskExmaine) {
            OverBreakRiskExmaine overBreakRiskExmaine = (OverBreakRiskExmaine) baseDomain
            return overBreakRiskExmaineRepository.findOverBreakRiskExmaineALlByNumberAndPage(overBreakRiskExmaine.getPage(), Integer.MAX_VALUE, overBreakRiskExmaine.getTunnelNumber())

        } else if (baseDomain instanceof RockOutburstRiskExamine) {
            RockOutburstRiskExamine rockOutburstRiskExamine = (RockOutburstRiskExamine) baseDomain
            return rockOutburstRiskExamineRepository.findRockOutburstRiskExamineALlByNumberAndPage(rockOutburstRiskExamine.getPage(), Integer.MAX_VALUE, rockOutburstRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof ShapeRiskExamine) {
            ShapeRiskExamine shapeRiskExamine = (ShapeRiskExamine) baseDomain
            return shapeRiskExamineRepository.findShapeRiskExamineALlByNumberAndPage(shapeRiskExamine.getPage(), Integer.MAX_VALUE, shapeRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof SurgeMudRiskExamine) {
            SurgeMudRiskExamine surgeMudRiskExamine = (SurgeMudRiskExamine) baseDomain
            return surgeMudRiskExamineRepository.findSurgeMudRiskExamineALlByNumberAndPage(surgeMudRiskExamine.getPage(), Integer.MAX_VALUE, surgeMudRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof TrafficAccidentRiskExamine) {
            TrafficAccidentRiskExamine trafficAccidentRiskExamine = (TrafficAccidentRiskExamine) baseDomain
            return trafficAccidentRiskExamineRepository.findTrafficAccidentRiskExamineALlByNumberAndPage(trafficAccidentRiskExamine.getPage(), Integer.MAX_VALUE, trafficAccidentRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof TunnelGrabageExamine) {
            TunnelGrabageExamine tunnelGrabageExamine = (TunnelGrabageExamine) baseDomain
            return tunnelGrabageExamineRepository.findTunnelGrabageExamineALlByNumberAndPage(tunnelGrabageExamine.getPage(), Integer.MAX_VALUE, tunnelGrabageExamine.getTunnelNumber())

        } else if (baseDomain instanceof TunnelHeadRiskExamine) {
            TunnelHeadRiskExamine tunnelHeadRiskExamine = (TunnelHeadRiskExamine) baseDomain
            return tunnelHeadRiskExamineRepository.findTunnelHeadRiskExamineALlByNumberAndPage(tunnelHeadRiskExamine.getPage(), Integer.MAX_VALUE, tunnelHeadRiskExamine.getTunnelNumber())

        } else if (baseDomain instanceof TunnelShallowCover) {
            TunnelShallowCover tunnelShallowCover = (TunnelShallowCover) baseDomain
            return tunnelShallowCoverRepository.findTunnelShallowCoverALlByNumberAndPage(tunnelShallowCover.getPage(), Integer.MAX_VALUE, tunnelShallowCover.getTunnelNumber())

        } else if (baseDomain instanceof TunnelExamineRelationQuery) {
            TunnelExamineRelationQuery tunnelExamineRelationQuery = (TunnelExamineRelationQuery) baseDomain

            return tunnelExmainRelationQueryCrudRepository.findByTunnelNumber(tunnelExamineRelationQuery.getTunnelNumber())
        }
        return new ArrayList<BaseDomain>()
    }
}