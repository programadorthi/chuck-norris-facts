import modules.ProjectModules

rootProject.name = "Chuck Norris Facts"

include(
    ProjectModules.APP,
    ProjectModules.Feature.Domain.FACTS,
    ProjectModules.Shared.DOMAIN,
    ProjectModules.Shared.NETWORK
)
