import modules.ProjectModules

rootProject.name = "Chuck Norris Facts"

include(
    ProjectModules.APP,
    ProjectModules.Shared.DOMAIN
)
