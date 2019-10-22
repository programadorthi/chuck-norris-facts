import modules.ProjectModules

rootProject.name = "Chuck Norris Facts"

include(
    ProjectModules.APP,
    ProjectModules.Feature.Data.FACTS,
    ProjectModules.Feature.Domain.FACTS,
    ProjectModules.Shared.DOMAIN,
    ProjectModules.Shared.NETWORK
)
